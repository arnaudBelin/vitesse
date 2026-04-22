package com.example.vitesse

import com.example.vitesse.data.entity.Candidate
import com.example.vitesse.utils.displayFirstName
import com.example.vitesse.utils.displayLastName
import java.time.Instant
import java.time.LocalDate
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CandidateExtensionsTest {

    @Test
    fun testDisplayFirstNameShouldCapitalizeFirstLetter() {
        // Given
        val candidate = getCandidate()

        // When
        val result = candidate.displayFirstName()

        // Then
        assertEquals("Garry", result)
    }

    @Test
    fun testDisplayLastNameShouldReturnUppercase() {
        // Given
        val candidate = getCandidate()

        // When
        val result = candidate.displayLastName()

        // Then
        assertEquals("HOLT", result)
    }

    fun getCandidate(): Candidate {
        return Candidate(
            firstName = "garry",
            lastName = "holt",
            phone = "1234567890",
            email = "exodus@example.com",
            birthDate = LocalDate.of(1990, 1, 1),
            createdAt = Instant.now(),
        )
    }
}
