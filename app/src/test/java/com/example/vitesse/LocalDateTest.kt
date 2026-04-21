package com.example.vitesse

import com.example.vitesse.utils.age
import java.time.LocalDate
import junit.framework.TestCase.assertEquals
import org.junit.Test

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
