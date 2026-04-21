package com.example.vitesse

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.vitesse.data.entity.Candidate
import com.example.vitesse.ui.navigation.Tab
import com.example.vitesse.ui.screen.CandidatesScreen
import java.time.Instant
import java.time.LocalDate
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class CandidatesScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun testCandidatesScreenFavoriteTabSelected() {
        // Given
        val favoriteCandidate =
            Candidate(
                id = 1,
                firstName = "Glenn",
                lastName = "Tipton",
                phone = "1234567890",
                email = "gt@example.com",
                birthDate = LocalDate.of(1990, 1, 1),
                isFavorite = true,
                createdAt = Instant.now(),
            )

        val nonFavoriteCandidate =
            Candidate(
                id = 2,
                firstName = "Rob",
                lastName = "Halford",
                phone = "0987654321",
                email = "rh@example.com",
                birthDate = LocalDate.of(1988, 5, 12),
                isFavorite = false,
                createdAt = Instant.now(),
            )

        // When
        composeTestRule.setContent {
            CandidatesScreen(
                candidates = listOf(favoriteCandidate, nonFavoriteCandidate),
                selectedTab = Tab.FAV,
                onTabSelected = {},
                onCandidateClick = {},
            )
        }

        // Then
        composeTestRule.onNodeWithText("Glenn").assertIsDisplayed()
        composeTestRule.onNodeWithText("TIPTON").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rob").assertDoesNotExist()
        composeTestRule.onNodeWithText("HALFORD").assertDoesNotExist()
    }

    @Test
    fun testCandidatesScreenAllTab() {
        // Given
        var selectedTab: Tab? = null

        composeTestRule.setContent {
            CandidatesScreen(
                candidates = emptyList(),
                selectedTab = Tab.FAV,
                onTabSelected = { tab -> selectedTab = tab },
                onCandidateClick = {},
            )
        }

        // When
        composeTestRule.onNodeWithTag("all_tab").performClick()

        // Then
        assertEquals(Tab.ALL, selectedTab)
    }

    @Test
    fun testCandidatesScreenFavoriteTab() {
        // Given
        var selectedTab: Tab? = null

        composeTestRule.setContent {
            CandidatesScreen(
                candidates = emptyList(),
                selectedTab = Tab.ALL,
                onTabSelected = { tab -> selectedTab = tab },
                onCandidateClick = {},
            )
        }

        // When
        composeTestRule.onNodeWithTag("favorite_tab").performClick()

        // Then
        assertEquals(Tab.FAV, selectedTab)
    }
}
