package com.example.vitesse

import app.cash.turbine.test
import com.example.vitesse.data.model.CurrencyRates
import com.example.vitesse.data.network.CurrencyClient
import com.example.vitesse.data.repository.CurrencyRepository
import com.example.vitesse.data.response.CurrencyResponse
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CurrencyRepositoryTest {

    @Test
    fun testFetchCurrencyDataMappedCurrencyModel() = runTest {
        // Given
        val fakeClient =
            object : CurrencyClient {
                override suspend fun getRates(): CurrencyResponse {
                    return CurrencyResponse(date = "2025-04-20", eur = CurrencyRates(gbp = 0.86))
                }
            }
        val repository = CurrencyRepository(fakeClient)

        // When / Then
        repository.fetchCurrencyData().test {
            val result = awaitItem()
            assertEquals(0.86, result.rates.gbp)
            awaitComplete()
        }
    }
}
