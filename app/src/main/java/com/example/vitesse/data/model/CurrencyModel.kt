package com.example.vitesse.data.model

data class CurrencyModel(
    val rates: CurrencyRates
)

data class CurrencyRates(
    val gbp: Double?
)
