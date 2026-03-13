package com.example.vitesse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vitesse.ui.component.SimpleSearchBar

@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            SimpleSearchBar(
                textFieldState = androidx.compose.foundation.text.input.TextFieldState(),
                onSearch = { /* Handle search */ },
                searchResults = listOf("Result 1", "Result 2", "Result 3"),
                modifier = Modifier.padding(16.dp)
            )
        },
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(innerPadding)
        ) {

        }
    }
}