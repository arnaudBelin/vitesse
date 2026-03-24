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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                val searchResults = viewModel.searchResultState.collectAsState(initial = listOf()).value
                val textFieldSearch = remember { TextFieldState() }

                Scaffold(
                    topBar = {
                        SimpleSearchBar(
                            textFieldState = textFieldSearch,
                            onSearch = { query ->
                                viewModel.searchCandidates(query)
                            },
                            onQueryChange = { query ->
                                viewModel.searchCandidates(query)
                            },
                            searchResults = searchResults,
                            onResultClick = { candidateId ->
                                println("Clicked candidate with ID: $candidateId")
                                viewModel.searchCandidates("")
                            },
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
