package cz.frank.rickandmorty.ui.detail

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.paging.*
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.domain.usecase.QueryCharactersUseCase
import cz.frank.rickandmorty.domain.usecase.QueryCharactersUseCaseParams
import cz.frank.rickandmorty.ui.search.QuerySearchCharactersIntent
import cz.frank.rickandmorty.ui.search.QuerySearchCharactersRoute
import cz.frank.rickandmorty.ui.search.QuerySearchCharactersViewModel
import cz.frank.rickandmorty.ui.search.QuerySearchScreen
import cz.frank.rickandmorty.utils.ui.CharacterList
import cz.frank.rickandmorty.utils.ui.ErrorScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.junit.Rule
import org.junit.Test

class QuerySearchScreenKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun querySearchErrorHandlingTest() {
        val pagingSource = object : PagingSource<Long, CharacterSimple>() {
            var iteration = 0
            override fun getRefreshKey(state: PagingState<Long, CharacterSimple>): Long {
                return 1L
            }

            override suspend fun load(params: LoadParams<Long>): LoadResult<Long, CharacterSimple> {
                val result =  if (iteration == 0) {
                    LoadResult.Error(Exception())
                } else LoadResult.Page<Long, CharacterSimple>(listOf(), null, null)
                iteration++
                return result
            }
        }
        val pager = Pager(PagingConfig(1), pagingSourceFactory =  { pagingSource })
        val useCase = object : QueryCharactersUseCase {
            override fun invoke(params: QueryCharactersUseCaseParams): Flow<PagingData<CharacterSimple>> {
                return pager.flow
            }
        }

       val vm = QuerySearchCharactersViewModel(
           useCase
       )
        composeTestRule.setContent {
            val navController = rememberNavController()
            QuerySearchCharactersRoute(
                navController, vm
            )
        }

        composeTestRule.waitUntil { composeTestRule.onNodeWithTag(ErrorScreen.SCREEN_TEST_TAG).isDisplayed() }
        composeTestRule.onNodeWithTag(ErrorScreen.RETRY_BUTTON_TEST_TAG).performClick()
        composeTestRule.waitUntil { composeTestRule.onNodeWithTag(QuerySearchScreen.SUCCESS_SCREEN).isDisplayed() }
    }

    @Test
    fun querySearchAppendErrorHandlingTest() {
        val pagingSource = object : PagingSource<Long, CharacterSimple>() {
            var iteration = 0
            override fun getRefreshKey(state: PagingState<Long, CharacterSimple>): Long {
                return 1L
            }

            override suspend fun load(params: LoadParams<Long>): LoadResult<Long, CharacterSimple> {
                val result =  if (iteration != 1) {
                        LoadResult.Page<Long, CharacterSimple>(listOf(), null, if (iteration == 2) null else iteration.toLong())
                } else LoadResult.Error(Exception())
                iteration++
                return result
            }
        }
        val pager = Pager(PagingConfig(1), pagingSourceFactory =  { pagingSource })
        val useCase = object : QueryCharactersUseCase {
            override fun invoke(params: QueryCharactersUseCaseParams): Flow<PagingData<CharacterSimple>> {
                return pager.flow
            }
        }

        val vm = QuerySearchCharactersViewModel(
            useCase
        )
        composeTestRule.setContent {
            val navController = rememberNavController()
            QuerySearchCharactersRoute(
                navController, vm
            )
        }

        composeTestRule.waitUntil { composeTestRule.onNodeWithTag(QuerySearchScreen.SUCCESS_SCREEN).isDisplayed() }
        composeTestRule.waitUntil { composeTestRule.onNodeWithTag(CharacterList.APPEND_ERROR_BUTTON_TEST_TAG).isDisplayed() }
        composeTestRule.onNodeWithTag(CharacterList.APPEND_ERROR_BUTTON_TEST_TAG).performClick()
        composeTestRule.waitUntil { composeTestRule.onNodeWithTag(CharacterList.APPEND_ERROR_BUTTON_TEST_TAG).isNotDisplayed() }
    }

    @Test
    fun querySearchChangeOfQuery() {
        val characters = listOf(
            CharacterSimple(1,"Rick Sanchez", "Alive", "https://rickandmortyapi.com/api/character/avatar/1.jpeg", true),
            CharacterSimple(3,"Morty Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/2.jpeg", true),
            CharacterSimple(4,"Summer Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/3.jpeg", true),
            CharacterSimple(5,"Beth Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/4.jpeg"),
            CharacterSimple(6,"Jerry Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/5.jpeg"),
            CharacterSimple(7,"Eric Stoltz Mask Morty", "Alive", "https://rickandmortyapi.com/api/character/avatar/6.jpeg"),
            CharacterSimple(8,"Abradolf Lincler", "Unknown", "https://rickandmortyapi.com/api/character/avatar/7.jpeg")
        )
        val pagingSource = object : PagingSource<Long, CharacterSimple>() {
            override fun getRefreshKey(state: PagingState<Long, CharacterSimple>): Long {
                return 1L
            }

            override suspend fun load(params: LoadParams<Long>): LoadResult<Long, CharacterSimple> {
                return LoadResult.Page(characters, null, null)
            }
        }
        val pager = Pager(PagingConfig(1), pagingSourceFactory =  { pagingSource })
        val useCase = object : QueryCharactersUseCase {
            override fun invoke(params: QueryCharactersUseCaseParams): Flow<PagingData<CharacterSimple>> {
                return pager.flow.map { if (params.query.isNotBlank()) it.filter { it.name.contains(params.query) } else it }
            }
        }

        val vm = QuerySearchCharactersViewModel(useCase)
        composeTestRule.setContent {
            val navController = rememberNavController()
            QuerySearchCharactersRoute(
                navController, vm
            )
        }
        composeTestRule.onNodeWithTag(QuerySearchScreen.SUCCESS_SCREEN).assertIsDisplayed()
        composeTestRule.waitUntil { composeTestRule.onNodeWithText(characters.first().name).isDisplayed() }
        for (character in characters) {
            composeTestRule.onNodeWithText(character.name).assertIsDisplayed()
        }
        val newQuery = "Beth Smit"
        vm.onIntent(QuerySearchCharactersIntent.OnQueryChanged(query = newQuery))
        composeTestRule.waitUntil { composeTestRule.onNodeWithText(characters.first { it.name != newQuery }.name).isNotDisplayed() }
        for (character in characters) {
            with(composeTestRule.onNodeWithText(character.name)) {
                if (character.name.contains(newQuery)) assertIsDisplayed() else assertIsNotDisplayed()
            }
        }
    }
}
