package com.example.vitesse.data.response

import com.example.vitesse.data.model.CurrencyModel
import com.example.vitesse.data.model.CurrencyRates
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyResponse(
    @Json(name = "date")
    val date: String,
    @Json(name = "eur")
    val eur: CurrencyRates,
) {
    fun toCurrencyModel(): CurrencyModel {
        return CurrencyModel(
            rates = eur
        )
    }
}
