package com.example.aichat

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.aichat.model.ModelInfo
import com.example.aichat.ui.components.ModelSelectionScreen
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class ModelSelectionScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displayModelsTest() {
        val testModels = listOf(
            ModelInfo("Llama 3", "llama3:8b"),
            ModelInfo("Mistral", "mistral")
        )
        var selectedModel: ModelInfo? = null

        composeTestRule.setContent {
            MaterialTheme {
                ModelSelectionScreen(
                    models = testModels,
                    onModelSelected = { selectedModel = it }
                )
            }
        }

        testModels.forEach { model ->
            composeTestRule.onNodeWithText(model.displayName).assertExists()
        }
    }

    @Test
    fun onModelSelectedTest() {
        val testModel = ModelInfo("Llama 3", "llama3:8b")
        var selectedModel: ModelInfo? = null

        composeTestRule.setContent {
            MaterialTheme {
                ModelSelectionScreen(
                    models = listOf(testModel),
                    onModelSelected = { selectedModel = it }
                )
            }
        }

        composeTestRule.onNodeWithText(testModel.displayName).performClick()

        Assert.assertEquals(testModel, selectedModel)
    }
}
