package be.business.androidpractice

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.business.androidpractice.activities.HomeActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<HomeActivity>()

    @Test
    fun verifyWelcomeTextDisplayed() {
        // Verify welcome message contains username passed in intent
        composeRule.onNodeWithText("Welcome", substring = true)
            .assertIsDisplayed()
    }
    @Test
    fun verifyInputFieldUpdates() {
        // Enter last name text
        val testInput = "Khan"
        composeRule.onNode(hasTestTag("email"))
            .performTextInput(testInput)

        // Check if text updated
        composeRule.onNode(hasTestTag("email"))
            .assertTextContains(testInput)
    }
    @Test
    fun verifyStateRestoresAfterRecreation() {
        // Type something
        val testInput = "Smith"
        composeRule.onNode(hasTestTag("email"))
            .performTextInput(testInput)

        // Simulate configuration change (recreate activity)
        composeRule.activityRule.scenario.recreate()

        // Verify restored text
        composeRule.onNode(hasTestTag("email"))
            .assertTextContains(testInput)
    }
}