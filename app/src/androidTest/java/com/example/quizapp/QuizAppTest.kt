package com.example.quizapp

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuizAppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testMainMenuToGallery() {
        // Use Espresso for XML buttons, then check transition with Compose
        onView(withId(R.id.buttonGallery)).perform(click())
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Add new entry").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Add new entry").assertIsDisplayed()
    }

    @Test
    fun testMainMenuToQuiz() {
        onView(withId(R.id.buttonQuiz)).perform(click())
        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText("Score:", substring = true).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Score:", substring = true).assertIsDisplayed()
    }
}

@RunWith(AndroidJUnit4::class)
class QuizActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<QuizActivity>()

    @Test
    fun testScoreUpdate() {
        // Wait for database items to load
        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithText("Score:", substring = true).fetchSemanticsNodes().isNotEmpty()
        }
        
        // Check initial score
        composeTestRule.onNodeWithText("Score: 0 / 0").assertIsDisplayed()

        // Identify the correct answer from the image's content description
        // The AsyncImage uses currentItem.name as its contentDescription
        val imageNode = composeTestRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.ContentDescription))
        val correctAnswer = imageNode.fetchSemanticsNode().config[SemanticsProperties.ContentDescription].first()

        // Click the button that matches the correct answer
        composeTestRule.onNodeWithText(correctAnswer).performClick()
        
        // Verify both score and total are updated (1 / 1)
        composeTestRule.onNodeWithText("Score: 1 / 1").assertIsDisplayed()

        
        // Verify feedback and Next button appear
        composeTestRule.onNodeWithText("Correct!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Next").assertIsDisplayed()


    }

    @Test
    fun testScoreWrong() {
        // Wait for database items to load
        composeTestRule.waitUntil(10000) {
            composeTestRule.onAllNodesWithText("Score:", substring = true).fetchSemanticsNodes().isNotEmpty()
        }

        // Check initial score
        composeTestRule.onNodeWithText("Score: 0 / 0").assertIsDisplayed()

        val imageNode = composeTestRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.ContentDescription))
        val correctAnswer = imageNode.fetchSemanticsNode().config[SemanticsProperties.ContentDescription].first()

        composeTestRule
            .onAllNodes(hasClickAction() and hasText(correctAnswer).not())
            .onFirst()
            .performClick()

        //val nodeText = wrongAnswer.toString()

        //composeTestRule.onNodeWithText(nodeText).performClick()

        composeTestRule.onNodeWithText("Score: 0 / 1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Wrong!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Next").assertIsDisplayed()
    }
}

@RunWith(AndroidJUnit4::class)
class GalleryActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<GalleryActivity>()

    @Test
    fun testAddEntryWithIntentStubbing() {
        Intents.init()
        try {
            val resultData = Intent()
            resultData.data = Uri.parse("android.resource://com.example.quizapp/" + R.drawable.italy)
            val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

           intending(hasAction(Intent.ACTION_OPEN_DOCUMENT)).respondWith(result)

            // Open the picker
            composeTestRule.onNodeWithText("Add new entry").performClick()
            
            // The dialog title
            composeTestRule.onNodeWithText("Add flag").assertIsDisplayed()
            
            // Enter name in the TextField with label "Country"
            composeTestRule.onNodeWithText("Country").performTextInput("Test Flag")
            
            // Click "Add" button in the dialog
            composeTestRule.onNode(hasText("Add")).performClick()
            
            // Verify it appeared in the list
            composeTestRule.onNodeWithText("Test Flag").assertIsDisplayed()
        } finally {
            Intents.release()
        }
    }
}
