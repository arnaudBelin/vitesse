package com.example.vitesse.ui.extension

import com.example.vitesse.data.entity.Candidate
import java.util.Locale

fun Candidate.displayFirstName(): String {
    val formattedFirstName = firstName.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }
    return formattedFirstName
}

fun Candidate.displayLastName(): String {
    val formattedLastName = lastName.uppercase(Locale.getDefault())
    return formattedLastName
}