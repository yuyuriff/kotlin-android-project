package com.example.aichat

//class ModelSelectionScreenTest {
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun displayModelsTest() {
//        val testModels = listOf(
//            ModelInfo("Llama 3", "llama3:8b"),
//            ModelInfo("Mistral", "mistral")
//        )
//        var selectedModel: ModelInfo? = null
//
//        composeTestRule.setContent {
//            MaterialTheme {
//                ModelSelectionScreen(
//                    models = testModels,
//                    onModelSelected = { selectedModel = it }
//                )
//            }
//        }
//
//        testModels.forEach { model ->
//            composeTestRule.onNodeWithText(model.displayName).assertExists()
//        }
//    }
//
//    @Test
//    fun onModelSelectedTest() {
//        val testModel = ModelInfo("Llama 3", "llama3:8b")
//        var selectedModel: ModelInfo? = null
//
//        composeTestRule.setContent {
//            MaterialTheme {
//                ModelSelectionScreen(
//                    models = listOf(testModel),
//                    onModelSelected = { selectedModel = it }
//                )
//            }
//        }
//
//        composeTestRule.onNodeWithText(testModel.displayName).performClick()
//
//        Assert.assertEquals(testModel, selectedModel)
//    }
//}
