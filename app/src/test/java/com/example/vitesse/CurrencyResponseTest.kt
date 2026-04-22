package com.example.vitesse

import com.example.vitesse.data.model.CurrencyRates
import com.example.vitesse.data.response.CurrencyResponse
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CurrencyResponseTest {

    @Test
    fun testToCurrencyModel() {
        // Given
        val response = CurrencyResponse(date = "2025-04-20", eur = CurrencyRates(gbp = 0.86))

        // When
        val result = response.toCurrencyModel()

        // Then
        assertEquals(0.86, result.rates.gbp)
    }
}
