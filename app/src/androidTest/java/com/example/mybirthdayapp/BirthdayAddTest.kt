package com.example.mybirthdayapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.mybirthdayapp.model.Birthday
import com.example.mybirthdayapp.screens.BirthdayAddScreen
import org.junit.Rule
import org.junit.Test

class BirthdayAddScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testBirthdayAddScreenAddsBirthdaySuccessfully() {
        var addedBirthday: Birthday? = null

        // Set the content for the test
        composeTestRule.setContent {
            BirthdayAddScreen(
                onAddBirthday = { birthday -> addedBirthday = birthday },
                onNavigateBack = {}
            )
        }

        // Fill out the fields
        composeTestRule.onNodeWithText("Name").performTextInput("John Doe")
        composeTestRule.onNodeWithText("Date").performTextInput("1")
        composeTestRule.onNodeWithText("Month").performTextInput("1")
        composeTestRule.onNodeWithText("Year").performTextInput("1990")
        composeTestRule.onNodeWithText("Picture URL").performTextInput("https://example.com/picture.jpg")
        composeTestRule.onNodeWithText("Remarks").performTextInput("Test Remarks")

        // Click the "Add Birthday" button
        composeTestRule.onNodeWithText("Add Birthday").performClick()

        // Verify that the birthday was added
        assert(addedBirthday != null)
        assert(addedBirthday?.name == "John Doe")
        assert(addedBirthday?.birthDayOfMonth == 1)
        assert(addedBirthday?.birthMonth == 1)
        assert(addedBirthday?.birthYear == 1990)
        assert(addedBirthday?.pictureUrl == "https://example.com/picture.jpg")
        assert(addedBirthday?.remarks == "Test Remarks")
    }
}