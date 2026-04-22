package com.example.vitesse.data.network

import com.example.vitesse.data.response.CurrencyResponse
import retrofit2.http.GET

interface CurrencyClient {
    @GET("currencies/eur.json") suspend fun getRates(): CurrencyResponse
}
