package com.example.vitesse.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.EuroSymbol
import androidx.compose.material.icons.outlined.PermIdentity
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.annotation.StringRes
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
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
     val firstName: Int? = null,
     val lastName: Int? = null,
     val phone: Int? = null,
     val email: Int? = null,
     val birthDate: Int? = null,
     val salary: Int? = null,
)

private fun validateInputs(formState: CandidateFormState): CandidateFormErrors {
    return CandidateFormErrors(
        firstName = if (formState.firstName.isBlank()) R.string.candidate_error_first_name_required else null,
        lastName = if (formState.lastName.isBlank()) R.string.candidate_error_last_name_required else null,
        phone = if (formState.phone.isBlank()) R.string.candidate_error_phone_required else null,
        email = if (formState.email.isBlank()) R.string.candidate_error_email_required else null,
        birthDate = when {
            formState.birthDate.isBlank() -> R.string.candidate_error_birth_date_required
            LocalDate.parse(formState.birthDate).isAfter(LocalDate.now().minusYears(18)) ->
                R.string.candidate_error_birth_date_adult
            else -> null
        },
        salary = when {
            formState.salary.isBlank() -> R.string.candidate_error_salary_required
            formState.salary.toDoubleOrNull() == null -> R.string.candidate_error_salary_invalid
            formState.salary.toDoubleOrNull()!! < 0 -> R.string.candidate_error_salary_positive
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

@Composable
private fun CandidateFormField(
    value: String,
    onValueChange: (String) -> Unit,
    labelRes: Int,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconPlaceholderWidth: Dp = 0.dp,
    errorMessageRes: Int? = null,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(12.dp))
        } else if (iconPlaceholderWidth > 0.dp) {
            Spacer(modifier = Modifier.width(iconPlaceholderWidth))
        }
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange,
            label = { Text(stringResource(labelRes)) },
            trailingIcon = trailingIcon,
            isError = errorMessageRes != null,
            supportingText = {
                errorMessageRes?.let { Text(stringResource(it)) }
            },
            readOnly = readOnly,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
        )
    }
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
                        text = stringResource(R.string.candidate_form_title_add),
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
                Text(text = stringResource(R.string.action_save))
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
                        Text(stringResource(R.string.action_confirm))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showBirthDatePicker = false
                        }
                    ) {
                        Text(stringResource(R.string.action_cancel))
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
            CandidateFormField(
                value = firstName,
                onValueChange = {
                    firstName = it
                },
                labelRes = R.string.candidate_form_first_name,
                icon = Icons.Outlined.PermIdentity,
                errorMessageRes = formErrors.firstName,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            )
            // lastName
            CandidateFormField(
                value = lastName,
                onValueChange = {
                    lastName = it
                },
                labelRes = R.string.candidate_form_last_name,
                iconPlaceholderWidth = 36.dp,
                errorMessageRes = formErrors.lastName,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            )
            // phone
            CandidateFormField(
                value = phone,
                onValueChange = {
                    phone = it
                },
                labelRes = R.string.candidate_form_phone,
                icon = Icons.Outlined.Phone,
                errorMessageRes = formErrors.phone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            )
            // email
            CandidateFormField(
                value = email,
                onValueChange = {
                    email = it
                },
                labelRes = R.string.candidate_form_email,
                icon = Icons.Outlined.Email,
                errorMessageRes = formErrors.email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            // birthDate
            CandidateFormField(
                value = birthDate,
                onValueChange = {},
                labelRes = R.string.candidate_form_birth_date,
                icon = Icons.Outlined.Cake,
                errorMessageRes = formErrors.birthDate,
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showBirthDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = stringResource(R.string.candidate_form_birth_date_select),
                        )
                    }
                },
            )
            // salary
            CandidateFormField(
                value = salary,
                onValueChange = {
                    salary = it
                },
                labelRes = R.string.candidate_form_salary_expectations,
                icon = Icons.Outlined.EuroSymbol,
                errorMessageRes = formErrors.salary,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )
            // note
            CandidateFormField(
                value = note,
                onValueChange = {
                    note = it
                },
                labelRes = R.string.candidate_form_note,
                icon = Icons.Outlined.Edit,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = false,
                minLines = 6,
                maxLines = 12,
            )
        }
    }
}
