package com.example.vitesse.data.repository

import android.util.Log
import coil.util.CoilUtils.result
import com.example.vitesse.data.model.CurrencyModel
import com.example.vitesse.data.network.CurrencyClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CurrencyRepository(private val client: CurrencyClient) {

    fun fetchCurrencyData(): Flow<CurrencyModel> =
        flow {
                val result = client.getRates()
                val model = result.toCurrencyModel()

                emit(model)
            }
            .catch { error -> Log.e("CurrencyRepository", error.message ?: "") }
}
