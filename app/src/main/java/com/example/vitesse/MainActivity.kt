package com.example.vitesse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vitesse.ui.component.SimpleSearchBar
import com.example.vitesse.ui.home.MainActivityViewModel
import com.example.vitesse.ui.navigation.Screen
import com.example.vitesse.ui.navigation.Tab
import com.example.vitesse.ui.screen.CandidateDetailsScreen
import com.example.vitesse.ui.screen.CandidatesScreen
import com.example.vitesse.ui.theme.VitesseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navHost = rememberNavController()
            VitesseTheme {
                CandidatesNavHost(
                    navHostController = navHost,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun CandidatesNavHost(
    navHostController: NavHostController,
    viewModel: MainActivityViewModel,
) {
    val startTab = Tab.ALL
    var selectedTab by rememberSaveable { mutableStateOf(startTab) }
    val searchResults = viewModel.searchResultState.collectAsState(initial = listOf()).value
    val candidates = viewModel.candidatesState.collectAsState(initial = listOf()).value
    val textFieldSearch = remember { TextFieldState() }

    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route
    ) {

        composable(route = Screen.Home.route) {
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
                            viewModel.searchCandidates("")
                            navHostController.navigate(
                                Screen.CandidateDetails.createRoute(candidateId.toString())
                            )
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                },
            ) { innerPadding ->
                CandidatesScreen(
                    modifier = Modifier.padding(innerPadding),
                    candidates = candidates,
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        selectedTab = tab
                    },
                    onCandidateClick = { candidateId ->
                        navHostController.navigate(
                            Screen.CandidateDetails.createRoute(candidateId.toString())
                        )
                    }
                )
            }
        }
        composable(
            route = Screen.CandidateDetails.route,
            arguments = Screen.CandidateDetails.navArguments
        ) {
            CandidateDetailsScreen(
                candidateId = it.arguments?.getString("candidateId") ?: "",
                onBackClick = { navHostController.navigateUp() },
                viewModel = viewModel
            )
        }
    }
}
