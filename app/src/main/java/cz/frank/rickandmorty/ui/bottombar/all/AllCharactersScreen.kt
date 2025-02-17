package cz.frank.rickandmorty.ui.bottombar.all

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import cz.frank.rickandmorty.R
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.ui.bottombar.all.navigation.AllCharactersNavDestination
import cz.frank.rickandmorty.ui.detail.navigation.DetailCharacterNavDestination
import cz.frank.rickandmorty.ui.search.navigation.QuerySearchedCharactersNavDestination
import cz.frank.rickandmorty.ui.theme.RickAndMortyTheme
import cz.frank.rickandmorty.utils.ui.CharacterList
import cz.frank.rickandmorty.utils.ui.ErrorScreen
import cz.frank.rickandmorty.utils.ui.ProcessEvents
import kotlinx.coroutines.flow.flowOf
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.allCharactersNavDestination(
    navHostController: NavHostController,
) {
    composable<AllCharactersNavDestination> {
        AllCharactersRoute(navHostController)
    }
}

@Composable
private fun AllCharactersRoute(
    navHostController: NavHostController,
    viewModel: AllCharactersViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val items = viewModel.allCharactersFlow.collectAsLazyPagingItems()
    viewModel.ProcessEvents {
        when (it) {
            AllCharactersEvent.GoBack -> navHostController.navigateUp()
            is AllCharactersEvent.GoToDetail -> navHostController.navigate(DetailCharacterNavDestination(it.id))
            is AllCharactersEvent.GoToQuerySearch -> navHostController.navigate(QuerySearchedCharactersNavDestination)
            is AllCharactersEvent.RetryRefresh -> items.retry()
        }
    }

    AllCharactersScreen(items, state, viewModel::onIntent)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllCharactersScreen(items: LazyPagingItems<CharacterSimple>, state: AllCharactersState, onIntent: (AllCharactersIntent) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Column(Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)) {
        Surface(Modifier.zIndex(2f), shadowElevation = 5.dp) { TopBar(onIntent, scrollBehavior) }
        Surface(Modifier.zIndex(1f).fillMaxSize()) {
            val uiState by rememberUpdatedState(getUIState(characters = items))
            AnimatedContent(uiState, label = "All characters screen content animation") {
                when (it) {
                    State.ERROR_AND_EMPTY -> ErrorScreen(onRetry = { onIntent(AllCharactersIntent.OnRetryTapped) })
                    State.SUCCESS -> CharacterList(items, onCharacterClick = { onIntent(AllCharactersIntent.OnItemTapped(it)) })
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onIntent: (AllCharactersIntent) -> Unit, scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.all_characters_title))
        },
        actions = {
            IconButton(onClick = { onIntent(AllCharactersIntent.OnSearchTapped) }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(R.string.all_characters_search_button_description),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

private fun getUIState(
    characters: LazyPagingItems<CharacterSimple>,
): State = when {
    characters.loadState.refresh is LoadState.Error && characters.itemCount == 0 -> State.ERROR_AND_EMPTY
    else -> State.SUCCESS
}

private enum class State {
    ERROR_AND_EMPTY, SUCCESS
}


@Preview
@Composable
private fun Preview() {
    val characters = listOf(
        CharacterSimple(1,"Rick Sanchez", "Alive", "https://rickandmortyapi.com/api/character/avatar/1.jpeg", true),
        CharacterSimple(3,"Morty Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/2.jpeg", true),
        CharacterSimple(4,"Summer Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/3.jpeg", true),
        CharacterSimple(5,"Beth Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/4.jpeg"),
        CharacterSimple(6,"Jerry Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/5.jpeg"),
        CharacterSimple(7,"Eric Stoltz Mask Morty", "Alive", "https://rickandmortyapi.com/api/character/avatar/6.jpeg"),
        CharacterSimple(8,"Abradolf Lincler", "Unknown", "https://rickandmortyapi.com/api/character/avatar/7.jpeg")
    )
    val state = AllCharactersState(false)
    val data = flowOf(PagingData.from(characters)).collectAsLazyPagingItems()
    RickAndMortyTheme { AllCharactersScreen(items = data,state) { } }
}
