package com.example.vitesse.ui.screen

import android.R.attr.onClick
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vitesse.data.entity.Candidate
import com.example.vitesse.ui.extension.displayFirstName
import com.example.vitesse.ui.extension.displayLastName
import com.example.vitesse.ui.home.MainActivityViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateDetailsScreen(
    candidateId: String,
    onBackClick: () -> Unit,
    viewModel: MainActivityViewModel,
) {
    LaunchedEffect(candidateId) {
        viewModel.getCandidateById(candidateId)
    }
    val candidate = viewModel.candidateState.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${candidate?.displayFirstName() } ${candidate?.displayLastName()}",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },

    ) {
            innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {

            candidate?.let {
                Text(text = "Name: ${it.firstName}")
                Text(text = "Email: ${it.email}")
                // Add more candidate details as needed
            } ?: Text(text = "Candidate not found")

            Button(onClick = onBackClick) {
                Text(text = "Back")
            }
        }

    }


}
