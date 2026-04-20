package com.example.vitesse

import com.example.vitesse.utils.age
import com.example.vitesse.utils.toLocalizedDisplayDate
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.LocalDate

class LocalDateTest {

    @Test
    fun testAgeShouldReturnExpectedYears() {
        // Given
        val birthDate = LocalDate.now().minusYears(20)

        // When
        val result = birthDate.age()

        // Then
        assertEquals(20, result)
    }
}