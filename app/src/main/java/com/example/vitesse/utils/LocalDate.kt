package com.example.vitesse.utils

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun LocalDate.toLocalizedDisplayDate(): String {
    return format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
}

fun LocalDate.age(): Int = Period.between(this, LocalDate.now()).years
