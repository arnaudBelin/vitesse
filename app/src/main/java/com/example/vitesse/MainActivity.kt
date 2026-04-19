package com.example.vitesse

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vitesse.data.entity.Candidate
import com.example.vitesse.data.model.CurrencyRates
import com.example.vitesse.ui.component.SimpleSearchBar
import com.example.vitesse.ui.home.MainActivityViewModel
import com.example.vitesse.ui.navigation.Screen
import com.example.vitesse.ui.navigation.Tab
import com.example.vitesse.ui.screen.CandidateAddOrUpdateScreen
import com.example.vitesse.ui.screen.CandidateDetailsScreen
import com.example.vitesse.ui.screen.CandidatesScreen
import com.example.vitesse.ui.theme.VitesseTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate

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

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currencyRates = viewModel.currencyState.collectAsState().value

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
                floatingActionButtonPosition = FabPosition.End,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navHostController.navigate(Screen.AddOrUpdateCandidate.createRoute(null))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.candidate_add_content_description),
                        )
                    }
                }
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
        ) { backStackEntry ->
            val candidateId =
                backStackEntry.arguments?.getString("candidateId") ?: return@composable
            val candidate = viewModel.candidateState.collectAsState().value

            LaunchedEffect(candidateId) {
                viewModel.getCandidateById(candidateId)
                viewModel.fetchCurrencyData()
            }

            CandidateDetailsScreen(
                candidate = candidate,
                currencyRates = currencyRates as CurrencyRates,
                snackbarHostState = snackbarHostState,
                onBackClick = { navHostController.navigateUp() },
                onEditClick = {
                    navHostController.navigate(
                        Screen.AddOrUpdateCandidate.createRoute(candidateId)
                    )
                },
                onRemoveClick = {
                    scope.launch {
                        val result = snackbarHostState
                            .showSnackbar(
                                message = context.getString(R.string.candidate_delete_confirmation),
                                actionLabel = context.getString(R.string.candidate_delete_action),
                                withDismissAction = true,
                                duration = SnackbarDuration.Indefinite
                            )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                candidate?.let {
                                    viewModel.deleteCandidate(candidate)
                                    val toast = Toast.makeText(
                                        context,
                                        context.getString(R.string.candidate_deleted),
                                        Toast.LENGTH_LONG
                                    )
                                    toast.show()
                                    navHostController.navigateUp()
                                }
                            }
                            else -> {}
                        }
                    }
                },
                onFavoriteClick = {
                    candidate?.let {
                        viewModel.toggleFavorite(it)
                        Toast.makeText(
                            context,
                            context.getString(
                                if (it.isFavorite) {
                                    R.string.candidate_favorite_removed
                                } else {
                                    R.string.candidate_favorite_added
                                }
                            ),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )
        }
        composable(
            route = Screen.AddOrUpdateCandidate.route
        ) { backStackEntry ->
            val candidateId = backStackEntry.arguments?.getString("candidateId")
            val selectedCandidate = viewModel.candidateState.collectAsState().value
            val candidate = if (candidateId == null) null else selectedCandidate

            candidateId?.let {
                LaunchedEffect(candidateId) {
                    viewModel.getCandidateById(candidateId)
                }
            }

            CandidateAddOrUpdateScreen(
                candidate = candidate,
                onBackClick = { navHostController.navigateUp() },
                onSaveClick = { formState ->
                    val isEditMode = candidate != null
                    viewModel.addOrUpdateCandidate(
                        candidate = Candidate(
                            id = candidate?.id ?: 0,
                            firstName = formState.firstName,
                            lastName = formState.lastName,
                            phone = formState.phone,
                            email = formState.email,
                            birthDate = LocalDate.parse(formState.birthDate),
                            salary = formState.salary.toDoubleOrNull(),
                            note = formState.note.ifBlank { null },
                            pictureUri = formState.pictureUri,
                            isFavorite = candidate?.isFavorite ?: false,
                            createdAt = candidate?.createdAt ?: Instant.now()
                        )
                    )
                    Toast.makeText(
                        context,
                        context.getString(
                            if (isEditMode) {
                                R.string.candidate_updated
                            } else {
                                R.string.candidate_created
                            }
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                    navHostController.navigateUp()
                }
            )
        }
    }
}
