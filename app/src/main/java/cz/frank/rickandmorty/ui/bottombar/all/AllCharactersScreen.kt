package cz.frank.rickandmorty.ui.bottombar.all

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import cz.frank.rickandmorty.R
import cz.frank.rickandmorty.domain.CharacterSimple
import cz.frank.rickandmorty.ui.bottombar.all.navigation.AllCharactersNavDestination
import cz.frank.rickandmorty.ui.detail.navigation.DetailCharacterNavDestination
import cz.frank.rickandmorty.ui.search.navigation.QuerySearchedCharactersNavDestination
import cz.frank.rickandmorty.ui.theme.RickAndMortyTheme
import cz.frank.rickandmorty.utils.ui.CharacterList
import cz.frank.rickandmorty.utils.ui.ProcessEvents
import kotlinx.collections.immutable.toImmutableList
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
    viewModel.ProcessEvents {
        when (it) {
            AllCharactersEvent.GoBack -> navHostController.navigateUp()
            is AllCharactersEvent.GoToDetail -> navHostController.navigate(DetailCharacterNavDestination(it.id))
            is AllCharactersEvent.GoToQuerySearch -> navHostController.navigate(QuerySearchedCharactersNavDestination)
        }
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    AllCharactersScreen(state, viewModel::onIntent)
}


@Composable
private fun AllCharactersScreen(state: State, onIntent: (AllCharactersIntent) -> Unit) {
    Column {
        Surface(Modifier.zIndex(2f), shadowElevation = 5.dp) { TopBar(onIntent) }
        Surface(Modifier.zIndex(1f).fillMaxSize()) {
            CharacterList(state.characterSimple, onCharacterClick = { onIntent(AllCharactersIntent.OnItemTapped(it)) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onIntent: (AllCharactersIntent) -> Unit) {
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
        }
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
    val state = State(characters.toImmutableList(), status = State.Status(loading = false))
    RickAndMortyTheme { AllCharactersScreen(state) { } }
}
