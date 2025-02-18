package cz.frank.rickandmorty.ui.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.rememberNavController
import cz.frank.rickandmorty.domain.model.Character
import cz.frank.rickandmorty.domain.usecase.*
import cz.frank.rickandmorty.utils.ui.ErrorScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.junit.Rule
import org.junit.Test

class DetailCharacterScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun detailScreenLoadingAndErrorTest() {
        val getCharacterUseCase = object : GetDetailCharacterUseCase {
            override fun invoke(params: GetDetailCharacterUseCaseParams): Flow<Result<Character>> {
                return flow {
                    params.refreshFlow.collect {
                        delay(200)
                        emit(Result.failure(Exception()))
                    }
                }
            }
        }

        val changeStatusUseCase = object : ChangeFavoriteStatusUseCase {
            override suspend fun invoke(params: ChangeFavoriteStatusUseCaseParams) {}
        }
        val vm = DetailCharacterViewModel(
            SavedStateHandle(mapOf("id" to 1L)),
            getCharacterUseCase,
            changeStatusUseCase
        )
        composeTestRule.setContent {
            val navController = rememberNavController()
            DetailCharacterRoute(
                navController, vm
            )
        }
        composeTestRule.onNodeWithTag(DetailCharacterScreen.LOADING_TEST_TAG).assertIsDisplayed()
        composeTestRule.waitUntil { composeTestRule.onNodeWithTag(ErrorScreen.SCREEN_TEST_TAG).isDisplayed() }
    }

    @Test
    fun detailScreenLoadingAndSuccessTest() {
        val getCharacterUseCase = object : GetDetailCharacterUseCase {
            override fun invoke(params: GetDetailCharacterUseCaseParams): Flow<Result<Character>> {
                return flow {
                    params.refreshFlow.collect {
                        delay(200)
                        emit(Result.success(Character(1, "", "", "", false, "", "", "", "", "")))
                    }
                }
            }
        }

        val changeStatusUseCase = object : ChangeFavoriteStatusUseCase {
            override suspend fun invoke(params: ChangeFavoriteStatusUseCaseParams) {}
        }
        val vm = DetailCharacterViewModel(
            SavedStateHandle(mapOf("id" to 1L)),
            getCharacterUseCase,
            changeStatusUseCase
        )
        composeTestRule.setContent {
            val navController = rememberNavController()
            DetailCharacterRoute(
                navController, vm
            )
        }
        composeTestRule.onNodeWithTag(DetailCharacterScreen.LOADING_TEST_TAG).assertIsDisplayed()
        composeTestRule.waitUntil { composeTestRule.onNodeWithTag(DetailCharacterScreen.SUCCESS_TEST_TAG).isDisplayed() }
    }

    @Test
    fun changeFavoriteStatus() {
        val isFavorite = MutableStateFlow(false)
        val getCharacterUseCase = object : GetDetailCharacterUseCase {
            override fun invoke(params: GetDetailCharacterUseCaseParams): Flow<Result<Character>> {
                return flow {
                    params.refreshFlow.collect {
                        emit(Result.success(Character(1, "", "", "", false, "", "", "", "", "")))
                    }
                }.combine(isFavorite) { character, isFavorite ->
                    character.map { it.copy(isFavorite = isFavorite) }
                }
            }
        }

        val changeStatusUseCase = object : ChangeFavoriteStatusUseCase {
            override suspend fun invoke(params: ChangeFavoriteStatusUseCaseParams) {
                isFavorite.update { params.isFavorite }
            }
        }
        val vm = DetailCharacterViewModel(
            SavedStateHandle(mapOf("id" to 1L)),
            getCharacterUseCase,
            changeStatusUseCase
        )
        composeTestRule.setContent {
            val navController = rememberNavController()
            DetailCharacterRoute(
                navController, vm
            )
        }
        composeTestRule.onNodeWithTag(DetailCharacterScreen.SUCCESS_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(DetailCharacterScreen.NOT_FAVORITE_ICON_TEST_TAG, useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag(DetailCharacterScreen.TOGGLE_BUT_TEST_TAG).performClick()
        composeTestRule.onNodeWithTag(DetailCharacterScreen.FAVORITE_ICON_TEST_TAG, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun detailScreenErrorAndRetryTest() {
        val getCharacterUseCase = object : GetDetailCharacterUseCase {
            override fun invoke(params: GetDetailCharacterUseCaseParams): Flow<Result<Character>> {
                var numberOfTries = 0
                return flow {
                    params.refreshFlow.collect {
                        delay(200)
                        if (numberOfTries == 0) emit(Result.failure(Exception()))
                        else emit(Result.success(Character(1, "", "", "", false, "", "", "", "", "")))
                        numberOfTries++
                    }
                }
            }
        }

        val changeStatusUseCase = object : ChangeFavoriteStatusUseCase {
            override suspend fun invoke(params: ChangeFavoriteStatusUseCaseParams) {}
        }
        val vm = DetailCharacterViewModel(
            SavedStateHandle(mapOf("id" to 1L)),
            getCharacterUseCase,
            changeStatusUseCase
        )
        composeTestRule.setContent {
            val navController = rememberNavController()
            DetailCharacterRoute(
                navController, vm
            )
        }

        composeTestRule.onNodeWithTag(DetailCharacterScreen.LOADING_TEST_TAG).assertIsDisplayed()

        composeTestRule.waitUntil { composeTestRule.onNodeWithTag(ErrorScreen.SCREEN_TEST_TAG).isDisplayed() }
        composeTestRule.onNodeWithTag(ErrorScreen.RETRY_BUTTON_TEST_TAG, useUnmergedTree = true).performClick()

        composeTestRule.onNodeWithTag(DetailCharacterScreen.LOADING_TEST_TAG).assertIsDisplayed()

        composeTestRule.waitUntil { composeTestRule.onNodeWithTag(DetailCharacterScreen.SUCCESS_TEST_TAG).isDisplayed() }
    }
}
