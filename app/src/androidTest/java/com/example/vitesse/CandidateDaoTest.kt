package com.example.vitesse

import app.cash.turbine.test
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vitesse.data.db.VitesseDB
import com.example.vitesse.data.entity.Candidate
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.time.LocalDate


@RunWith(AndroidJUnit4::class)
class CandidateDaoTest {
    private lateinit var database: VitesseDB


    @Before
    fun createDb() {
        database = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                VitesseDB::class.java
            )
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testShouldInsertCandidateIntoDatabaseSuccessfully() {
        runTest {
            // Given
            val candidate = Candidate(
                firstName = "John",
                lastName = "Doe",
                phone = "1234567890",
                email = "john@example.com",
                birthDate = LocalDate.of(1972, 1, 1),
                salary = 8000.0,
                note = "A good candidate but expensive",
                isFavorite = true,
                createdAt = Instant.now()
            )

            // When
            database.candidateDao().addOrUpdateCandidate(candidate)

            // Then
            val insertedAnimal = database.candidateDao().getCandidateById(1)
            assertEquals(candidate.firstName, insertedAnimal?.firstName)
            assertEquals(candidate.lastName, insertedAnimal?.lastName)
            assertEquals(candidate.phone, insertedAnimal?.phone)
            assertEquals(candidate.email, insertedAnimal?.email)
            assertEquals(candidate.birthDate, insertedAnimal?.birthDate)
            assertEquals(candidate.salary, insertedAnimal?.salary)
            assertEquals(candidate.note, insertedAnimal?.note)
            assertEquals(candidate.isFavorite, insertedAnimal?.isFavorite)
        }
    }

    @Test
    fun testShouldUpdateCandidateIntoDatabaseSuccessfully() = runTest {
        // Given
        val candidate = Candidate(
            firstName = "John",
            lastName = "Doe",
            phone = "1234567890",
            email = "john@example.com",
            birthDate = LocalDate.of(1972, 1, 1),
            salary = 8000.0,
            note = "A good candidate but expensive",
            isFavorite = true,
            createdAt = Instant.now()
        )

        database.candidateDao().addOrUpdateCandidate(candidate)

        // When
        val insertedCandidate = database.candidateDao().getCandidateById(1)
        val updatedInput = insertedCandidate?.copy(firstName = "Max")
        updatedInput?.let {
            database.candidateDao().addOrUpdateCandidate(it)
        }

        // Then
        val updatedCandidate = database.candidateDao().getCandidateById(1)
        assertEquals("Max", updatedCandidate?.firstName)
    }


    @Test
    fun testShouldDeleteCandidateFromDatabaseSuccessfully() {
        runTest {
            // Given
            val candidate = Candidate(
                firstName = "John",
                lastName = "Doe",
                phone = "1234567890",
                email = "john@example.com",
                birthDate = LocalDate.of(1972, 1, 1),
                salary = 8000.0,
                note = "A good candidate but expensive",
                isFavorite = true,
                createdAt = Instant.now()
            )
            database.candidateDao().addOrUpdateCandidate(candidate)

            // When
            val insertedCandidate = database.candidateDao().getCandidateById(1)
            insertedCandidate?.let {
                database.candidateDao().deleteCandidate(it)
            }

            // Then
            val deletedCandidate = database.candidateDao().getCandidateById(1)
            assertNull(deletedCandidate)
        }
    }

    @Test
    fun testGetAllCandidatesShouldReturnEmptyList() = runTest {
        database.candidateDao().getAllCandidates().test {
            val candidates = awaitItem()
            assertTrue(candidates.isEmpty())
            cancel()
        }
    }

    @Test
    fun testGetAllCandidatesShouldReturnListOfCandidates() = runTest {
        // Given
        val candidates = listOf(
            Candidate(
                firstName = "John",
                lastName = "Doe",
                phone = "1234567890",
                email = "john@example.com",
                birthDate = LocalDate.of(1972, 1, 1),
                salary = 8000.0,
                note = "A good candidate but expensive",
                isFavorite = true,
                createdAt = Instant.now()
            ),
            Candidate(
                firstName = "Jane",
                lastName = "Smith",
                phone = "0987654321",
                email = "jane@example.com",
                birthDate = LocalDate.of(1985, 5, 15),
                salary = 6000.0,
                note = "A promising candidate with a good track record",
                isFavorite = false,
                createdAt = Instant.now(),
            )
        )


        candidates.forEach { database.candidateDao().addOrUpdateCandidate(it) }

        // When
        database.candidateDao().getAllCandidates().test {
            // Then
            val results = awaitItem()
            assertEquals(candidates.size, results.size)
            cancel()
        }
    }

    @Test
    fun testSearchCandidatesShouldOnlyReturnFavoritesWhenFavoritesOnlyIsTrue() = runTest {
        // Given
        val candidateOne = Candidate(
            firstName = "Dave",
            lastName = "Mustaine",
            phone = "1234567890",
            email = "mustaine@example.com",
            birthDate = LocalDate.of(1990, 3, 10),
            salary = 5000.0,
            note = "Favorite matching candidate",
            isFavorite = true,
            createdAt = Instant.now()
        )
        val candidateTwo = Candidate(
            firstName = "Dave",
            lastName = "Lombardo",
            phone = "0987654321",
            email = "lombardo@example.com",
            birthDate = LocalDate.of(1988, 7, 8),
            salary = 4500.0,
            note = "Non favorite matching candidate",
            isFavorite = false,
            createdAt = Instant.now()
        )
        val candidateThree = Candidate(
            firstName = "Steve",
            lastName = "Harris",
            phone = "1111111111",
            email = "vh@example.com",
            birthDate = LocalDate.of(1992, 11, 4),
            salary = 4700.0,
            note = "Favorite non matching candidate",
            isFavorite = true,
            createdAt = Instant.now()
        )

        database.candidateDao().addOrUpdateCandidate(candidateOne)
        database.candidateDao().addOrUpdateCandidate(candidateTwo)
        database.candidateDao().addOrUpdateCandidate(candidateThree)

        // When
        val results = database.candidateDao().getCandidatesBySearch(
            query = "Dav",
            favoritesOnly = true
        )

        // Then
        assertEquals(1, results.size)
        assertEquals("Mustaine", results.single().lastName)
        assertTrue(results.all { it.isFavorite })
    }
}
