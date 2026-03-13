package com.example.vitesse.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vitesse.R

enum class Destination(
    val route: String,
    val label: Int,
) {
    ALL(
        "all",
        R.string.tab_all
    ),
    FAV(
        "favorites",
        R.string.tab_fav
    ),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTab(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Destination.ALL
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(
        modifier = modifier,
        topBar = {
            PrimaryTabRow(selectedTabIndex = selectedDestination) {
                Destination.entries.forEachIndexed { index, destination ->
                    Tab(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
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
        AppNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.ALL -> Allscreen()
                    Destination.FAV -> FavScreen()
                }
            }
        }
    }
}


@Composable
fun Allscreen() {
    Text("All")
}

@Composable
fun FavScreen() {
    Text("Favorites")
}
