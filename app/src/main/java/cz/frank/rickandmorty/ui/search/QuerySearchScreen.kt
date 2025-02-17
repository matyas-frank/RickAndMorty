package cz.frank.rickandmorty.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import cz.frank.rickandmorty.R
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.ui.detail.navigation.DetailCharacterNavDestination
import cz.frank.rickandmorty.ui.search.navigation.QuerySearchedCharactersNavDestination
import cz.frank.rickandmorty.ui.theme.RickAndMortyTheme
import cz.frank.rickandmorty.utils.ui.CharacterList
import cz.frank.rickandmorty.utils.ui.ProcessEvents
import kotlinx.coroutines.flow.flowOf
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.querySearchCharactersNavDestination(
    navHostController: NavHostController,
) {
    composable<QuerySearchedCharactersNavDestination> {
        QuerySearchCharactersRoute(navHostController)
    }
}

@Composable
private fun QuerySearchCharactersRoute(
    navHostController: NavHostController,
    viewModel: QuerySearchCharactersViewModel = koinViewModel()
) {
    viewModel.ProcessEvents {
        when (it) {
            QuerySearchCharactersEvent.GoBack -> navHostController.navigateUp()
            is QuerySearchCharactersEvent.GoToDetail -> navHostController.navigate(DetailCharacterNavDestination(it.id))
        }
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val characters = viewModel.charactersFlow.collectAsLazyPagingItems()
    QuerySearchCharactersScreen(characters, state, viewModel::onIntent)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuerySearchCharactersScreen(
    characters: LazyPagingItems<CharacterSimple>,
    state: QuerySearchCharactersState,
    onIntent: (QuerySearchCharactersIntent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Surface(Modifier.zIndex(2f), shadowElevation = 5.dp) { TopBar(state.query, onIntent, scrollBehavior) }
        }
    ) {
        Surface(Modifier.zIndex(1f).fillMaxSize().padding(it)) {
            CharacterList(
                characters,
                areCharacterCardsTransparent = true,
                onCharacterClick = { onIntent(QuerySearchCharactersIntent.OnItemTapped(it)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(query: String, onIntent: (QuerySearchCharactersIntent) -> Unit, scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = {
            InputField(
                query = query,
                onQueryChange = { onIntent(QuerySearchCharactersIntent.OnQueryChanged(it)) },
                onSearch = { /* Not necessary because search is immediate after typing */ },
                expanded = true,
                onExpandedChange = { /* Search view is permanent */ },
                placeholder = { Text(stringResource(R.string.search_characters_input_field_placeholder)) },
            )
        },
        navigationIcon = {
            IconButton(onClick = { onIntent(QuerySearchCharactersIntent.OnBackTapped) }) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, stringResource(R.string.go_back_description))
            }
        },
        actions = {
            AnimatedVisibility(query.isNotBlank()) {
                IconButton(onClick = { onIntent(QuerySearchCharactersIntent.OnClearQueryTapped) }) {
                    Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.search_characters_clear_query_description))
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
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
    val state = QuerySearchCharactersState(status = QuerySearchCharactersState.Status(loading = false), "")
    RickAndMortyTheme { QuerySearchCharactersScreen (flowOf(PagingData.from(characters)).collectAsLazyPagingItems(), state) { } }
}

