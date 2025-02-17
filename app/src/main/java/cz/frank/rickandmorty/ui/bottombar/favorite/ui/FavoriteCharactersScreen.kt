package cz.frank.rickandmorty.ui.bottombar.favorite.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import cz.frank.rickandmorty.ui.bottombar.favorite.navigation.FavoriteCharactersNavDestination
import cz.frank.rickandmorty.ui.detail.navigation.DetailCharacterNavDestination
import cz.frank.rickandmorty.ui.theme.RickAndMortyTheme
import cz.frank.rickandmorty.utils.ui.CharacterList
import cz.frank.rickandmorty.utils.ui.ProcessEvents
import cz.frank.rickandmorty.utils.ui.Space
import kotlinx.coroutines.flow.flowOf
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.favoriteCharactersNavDestination(
    navHostController: NavHostController,
) {
    composable<FavoriteCharactersNavDestination> {
        FavoriteCharactersRoute(navHostController)
    }
}

@Composable
private fun FavoriteCharactersRoute(
    navHostController: NavHostController,
    viewModel: FavoriteCharactersViewModel = koinViewModel()
) {
    viewModel.ProcessEvents {
        when (it) {
            is FavoriteCharactersEvent.GoToDetail -> navHostController.navigate(DetailCharacterNavDestination(it.id))
        }
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val characters = viewModel.favoriteCharactersFlow.collectAsLazyPagingItems()
    AllCharactersScreen(characters, state, viewModel::onIntent)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllCharactersScreen(characters: LazyPagingItems<CharacterSimple>, state: FavoriteCharactersState, onIntent: (FavoriteCharactersIntent) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Column(Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)) {
        Surface(Modifier.zIndex(2f), shadowElevation = 5.dp) { TopBar(scrollBehavior) }
        Surface(
            Modifier
                .zIndex(1f)
                .fillMaxSize()
        ) {
            val uiState by rememberUpdatedState(state.getUIState(characters))
            AnimatedContent(targetState = uiState, label = "Favorite screen content animation") {
                when (it) {
                    State.EMPTY -> EmptyScreen()
                    State.LOADING -> LoadingScreen()
                    State.NOT_EMPTY -> CharacterList(characters, onCharacterClick = { onIntent(FavoriteCharactersIntent.OnItemTapped(it)) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.favorite_characters_title))
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun EmptyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Space.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.favorite_characters_no_favorites_headline),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(Space.small))
        Text(
            text = stringResource(R.string.favorite_characters_no_favorites_support_message),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun LoadingScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

private fun FavoriteCharactersState.getUIState(characters: LazyPagingItems<CharacterSimple>) = when {
    loading -> State.LOADING
    characters.itemCount == 0 -> State.EMPTY
    else -> State.NOT_EMPTY
}

private enum class State {
    EMPTY, NOT_EMPTY, LOADING
}

@Preview
@Composable
private fun Preview() {
    val characters = listOf(
        CharacterSimple(1,"Rick Sanchez", "Alive", "https://rickandmortyapi.com/api/character/avatar/1.jpeg", true),
        CharacterSimple(3,"Morty Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/2.jpeg", true),
        CharacterSimple(4,"Summer Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/3.jpeg", true),
        CharacterSimple(5,"Beth Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/4.jpeg", true),
        CharacterSimple(6,"Jerry Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/5.jpeg", true),
        CharacterSimple(7,"Eric Stoltz Mask Morty", "Alive", "https://rickandmortyapi.com/api/character/avatar/6.jpeg", true),
        CharacterSimple(8,"Abradolf Lincler", "Unknown", "https://rickandmortyapi.com/api/character/avatar/7.jpeg", true)
    )
    val state = FavoriteCharactersState(loading = false)
    RickAndMortyTheme { AllCharactersScreen(flowOf(PagingData.from(characters)).collectAsLazyPagingItems(), state) { } }
}
