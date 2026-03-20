package com.example.vitesse.ui.navigation

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