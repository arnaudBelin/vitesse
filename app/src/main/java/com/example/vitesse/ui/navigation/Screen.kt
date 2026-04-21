package com.example.vitesse.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val navArguments: List<NamedNavArgument> = emptyList()) {
    data object Home : Screen("home")

    data object CandidateDetails :
        Screen(
            route = "candidateDetails/{candidateId}",
            navArguments = listOf(navArgument("candidateId") { type = NavType.StringType }),
        ) {
        fun createRoute(candidateId: String) = "candidateDetails/$candidateId"
    }

    data object AddOrUpdateCandidate :
        Screen(
            route = "addOrEditCandidate?candidateId={candidateId}",
            navArguments =
                listOf(
                    navArgument("candidateId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                ),
        ) {
        fun createRoute(candidateId: String? = null): String {
            return if (candidateId == null) {
                "addOrEditCandidate"
            } else {
                "addOrEditCandidate?candidateId=$candidateId"
            }
        }
    }
}
