package com.example.vitesse.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vitesse.data.entity.Candidate
import com.example.vitesse.ui.navigation.Destination
import com.example.vitesse.ui.screen.CandidatesScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTab(
    modifier: Modifier = Modifier,
    selectedDestination: Destination,
    onDestinationSelected: (Destination) -> Unit,
    candidates: List<Candidate>
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier,
        topBar = {
            PrimaryTabRow(selectedTabIndex = selectedDestination.ordinal) {
                Destination.entries.forEachIndexed { index, destination ->
                    Tab(
                        selected = selectedDestination.ordinal == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            onDestinationSelected(destination)
                        },
                        text = {
                            Text(
                                text = stringResource(destination.label),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
        }
    ) { contentPadding ->
        NavHost(
            navController,
            startDestination = Destination.ALL.route,
            modifier = Modifier.padding(contentPadding)
        ) {
            Destination.entries.forEach { destination ->

                composable(destination.route) {
                    when (destination) {
                        Destination.ALL -> CandidatesScreen(
                            modifier = Modifier.padding(contentPadding),
                            candidates
                        )
                        Destination.FAV -> CandidatesScreen(
                            modifier = Modifier.padding(contentPadding),
                            candidates.filter { it.isFavorite }
                        )
                    }
                }
            }
        }
    }
}
