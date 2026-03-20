package com.example.vitesse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vitesse.data.entity.Candidate
import com.example.vitesse.ui.component.NavigationTab
import com.example.vitesse.ui.component.SimpleSearchBar
import com.example.vitesse.ui.home.MainActivityViewModel
import com.example.vitesse.ui.navigation.Destination
import com.example.vitesse.ui.theme.VitesseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VitesseTheme {
                val startDestination = Destination.ALL
                var selectedDestination by rememberSaveable { mutableStateOf(startDestination) }

                Scaffold(
                    topBar = {
                        SimpleSearchBar(
                            textFieldState = TextFieldState(),
                            onSearch = { /* Handle search */ },
                            searchResults = listOf("Result 1", "Result 2", "Result 3"),
                            modifier = Modifier.padding(16.dp)
                        )
                    },
                ) { innerPadding ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavigationTab(
                            modifier = Modifier.fillMaxSize(),
                            selectedDestination = selectedDestination,
                            onDestinationSelected = { destination ->
                                selectedDestination = destination
                            },
                            candidates = viewModel.candidateState.collectAsState(initial = listOf()).value

                        )
                    }
                }
            }
        }
    }
}