package com.example.vitesse.ui.screen

import android.R.attr.height
import android.R.attr.name
import android.R.attr.text
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Abc
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.EuroSymbol
import androidx.compose.material.icons.outlined.PermIdentity
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.vitesse.R
import com.example.vitesse.data.entity.Candidate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private data class CandidateFormState(
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val email: String = "",
    val birthDate: String = "",
    val salary: String = "",
    val note: String = "",
)

private data class CandidateFormErrors(
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val birthDate: String? = null,
    val salary: String? = null,
)

private fun validateInputs(formState: CandidateFormState): CandidateFormErrors {
    return CandidateFormErrors(
        firstName = if (formState.firstName.isBlank()) "Le prénom est obligatoire" else null,
        lastName = if (formState.lastName.isBlank()) "Le nom est obligatoire" else null,
        phone = if (formState.phone.isBlank()) "Le téléphone est obligatoire" else null,
        email = if (formState.email.isBlank()) "L'email est obligatoire" else null,
        birthDate = when {
            formState.birthDate.isBlank() -> "La date de naissance est obligatoire"
            LocalDate.parse(formState.birthDate).isAfter(LocalDate.now().minusYears(18)) ->
                "Le candidat doit être majeur"
            else -> null
        },
        salary = when {
            formState.salary.isBlank() -> "Le salaire est obligatoire"
            formState.salary.toDoubleOrNull() == null -> "Le salaire est invalide"
            formState.salary.toDoubleOrNull()!! < 0 -> "Le salaire doit être positif"
            else -> null
        },
    )
}

private fun birthDateToMillis(date: String): Long? {
    return runCatching {
        LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()
    }.getOrNull()
}

private fun millisToBirthDate(millis: Long): String {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
        .format(DateTimeFormatter.ISO_LOCAL_DATE)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateAddOrUpdateScreen(
    candidate: Candidate?,
    onBackClick: () -> Unit,
    onSaveClick: (Double) -> Unit,
) {

    // State
    var firstName by rememberSaveable { mutableStateOf(candidate?.firstName ?: "") }
    var lastName by rememberSaveable { mutableStateOf(candidate?.lastName ?: "") }
    var phone by rememberSaveable { mutableStateOf(candidate?.phone ?: "") }
    var email by rememberSaveable { mutableStateOf(candidate?.email ?: "") }
    var birthDate by rememberSaveable { mutableStateOf(candidate?.birthDate?.toString() ?: "") }
    var salary by rememberSaveable { mutableStateOf(candidate?.salary?.toString() ?: "") }
    var note by rememberSaveable { mutableStateOf(candidate?.note ?: "") }
    var formErrors by remember { mutableStateOf(CandidateFormErrors()) }
    var showBirthDatePicker by remember { mutableStateOf(false) }

    val birthDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = birthDateToMillis(birthDate)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ajouter un candidat",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    val formState = CandidateFormState(
                        firstName = firstName,
                        lastName = lastName,
                        phone = phone,
                        email = email,
                        birthDate = birthDate,
                        salary = salary,
                        note = note,
                    )
                    formErrors = validateInputs(formState)
                    if (formErrors == CandidateFormErrors()) {
                        onSaveClick(salary.toDouble())
                    }
                }
            ) {
                Text(text = "Sauvegarder")
            }
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        if (showBirthDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    showBirthDatePicker = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            birthDatePickerState.selectedDateMillis?.let { selectedDate ->
                                birthDate = millisToBirthDate(selectedDate)
                            }
                            showBirthDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showBirthDatePicker = false
                        }
                    ) {
                        Text("Annuler")
                    }
                }
            ) {
                DatePicker(state = birthDatePickerState)
            }
        }

        Column(
            Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            // firstName
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = firstName,
                onValueChange = {
                    firstName = it
                },
                label = { Text("Prénom") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.PermIdentity,
                        contentDescription = stringResource(R.string.action_edit),
                    )
                },
                isError = formErrors.firstName != null,
                supportingText = {
                    formErrors.firstName?.let { Text(it) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
            // lastName
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = lastName,
                onValueChange = {
                    lastName = it
                },
                label = { Text("Nom") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.PermIdentity,
                        contentDescription = stringResource(R.string.action_edit),
                    )
                },
                isError = formErrors.lastName != null,
                supportingText = {
                    formErrors.lastName?.let { Text(it) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
            // phone
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phone,
                onValueChange = {
                    phone = it
                },
                label = { Text("Téléphone") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = stringResource(R.string.action_edit),
                    )
                },
                isError = formErrors.phone != null,
                supportingText = {
                    formErrors.phone?.let { Text(it) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )
            // email
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = {
                    email = it
                },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = stringResource(R.string.action_edit),
                    )
                },
                isError = formErrors.email != null,
                supportingText = {
                    formErrors.email?.let { Text(it) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
            // birthDate
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = birthDate,
                onValueChange = {},
                label = { Text("Date de naissance") },
                trailingIcon = {
                    IconButton(onClick = { showBirthDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Sélectionner une date",
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Cake,
                        contentDescription = stringResource(R.string.action_edit),
                    )
                },
                isError = formErrors.birthDate != null,
                supportingText = {
                    formErrors.birthDate?.let { Text(it) }
                },
                readOnly = true,
                singleLine = true
            )
            // salary
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = salary,
                onValueChange = {
                    salary = it
                },
                label = { Text("Salaire") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.EuroSymbol,
                        contentDescription = stringResource(R.string.action_edit),
                    )
                },
                isError = formErrors.salary != null,
                supportingText = {
                    formErrors.salary?.let { Text(it) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            // note
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = note,
                onValueChange = {
                    note = it
                },
                label = { Text("Note") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Abc,
                        contentDescription = stringResource(R.string.action_edit),
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                minLines = 6,
                maxLines = 12,
            )
        }

    }
}
