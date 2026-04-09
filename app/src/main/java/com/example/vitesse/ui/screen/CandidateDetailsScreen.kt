package com.example.vitesse.ui.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vitesse.R
import com.example.vitesse.data.entity.Candidate
import com.example.vitesse.utils.age
import com.example.vitesse.utils.displayFirstName
import com.example.vitesse.utils.displayLastName
import com.example.vitesse.utils.toLocalizedDisplayDate
import java.text.NumberFormat
import java.util.Currency
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateDetailsScreen(
    candidate: Candidate?,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${candidate?.displayFirstName().orEmpty()} ${candidate?.displayLastName().orEmpty()}".trim(),
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
                },
                actions = {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (candidate?.isFavorite == true) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = stringResource(R.string.action_favorite),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = stringResource(R.string.action_edit),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onRemoveClick) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.action_delete),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        val padding = 16.dp
        val scrollState = rememberScrollState()
        val salaryFormatter = NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance("EUR")
            maximumFractionDigits = 0
        }
        val birthdayValue = candidate?.birthDate?.let { birthDate ->
            stringResource(
                R.string.candidate_details_birthday_value,
                birthDate.toLocalizedDisplayDate(),
                stringResource(R.string.candidate_age_years, birthDate.age())
            )
        }.orEmpty()
        val salaryValue = candidate?.salary?.let { salaryFormatter.format(it) }.orEmpty()
        val salaryHint = candidate?.salary?.let {
            stringResource(R.string.candidate_details_salary_hint, salaryFormatter.format(it / 1000))
        }.orEmpty()

        Column(
            Modifier
                .padding(innerPadding)
                .padding(padding)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                if (candidate?.pictureUri.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .size(200.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.candidate_picture),
                            tint = Color.White,
                            modifier = Modifier.size(200.dp)
                        )
                    }
                } else {
                    AsyncImage(
                        model = candidate?.pictureUri,
                        contentDescription = stringResource(R.string.candidate_picture),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .size(200.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            )
            {
                CTAContent(
                    icon = Icons.Default.Phone,
                    detail = stringResource(R.string.cta_call),
                    onClick = {
                        // phone call
                        val phoneNumber = candidate?.phone ?: return@CTAContent
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = "tel:$phoneNumber".toUri()
                        }
                        context.startActivity(intent)
                    }
                )
                CTAContent(
                    icon = Icons.AutoMirrored.Filled.Message,
                    detail = stringResource(R.string.cta_sms),
                    onClick = {
                        // send SMS
                        val phoneNumber = candidate?.phone ?: return@CTAContent
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "smsto:$phoneNumber".toUri()
                        }
                        context.startActivity(intent)
                    }
                )
                CTAContent(
                    icon = Icons.Filled.Email,
                    detail = stringResource(R.string.cta_email),
                    onClick = {
                        // send email
                        val email = candidate?.email ?: return@CTAContent
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:$email".toUri()
                        }
                        context.startActivity(intent)
                     }
                )
            }

            DetailCard(
                title = stringResource(R.string.candidate_details_about),
                content = birthdayValue,
                hint = stringResource(R.string.candidate_details_birthday_hint)
            )

            DetailCard(
                title = stringResource(R.string.candidate_details_salary_title),
                content = salaryValue,
                hint = salaryHint
            )

            DetailCard(
                title = stringResource(R.string.candidate_details_notes_title),
                content = candidate?.note.orEmpty(),
            )
        }
    }
}

@Composable
private fun CTAContent(
    icon: ImageVector,
    detail: String,
    onClick: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        IconButton(
            modifier = Modifier
                .size(48.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = "CTA Icon",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = detail,
            modifier = Modifier.padding(top = 6.dp),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun DetailCard(
    title: String,
    content: String,
    hint: String? = null,
) {
    Surface(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 0.dp,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = content,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (!hint.isNullOrEmpty()) {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
