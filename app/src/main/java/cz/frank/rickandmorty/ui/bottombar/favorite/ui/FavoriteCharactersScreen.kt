package cz.frank.rickandmorty.ui.bottombar.favorite.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.ui.bottombar.favorite.navigation.FavoriteCharactersNavDestination
import cz.frank.rickandmorty.ui.detail.navigation.DetailCharacterNavDestination
import cz.frank.rickandmorty.ui.theme.RickAndMortyTheme
import cz.frank.rickandmorty.utils.ui.CharacterList
import cz.frank.rickandmorty.utils.ui.ProcessEvents
import kotlinx.collections.immutable.toImmutableList
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

    AllCharactersScreen(state, viewModel::onIntent)
}


@Composable
private fun AllCharactersScreen(state: FavoriteCharactersState, onIntent: (FavoriteCharactersIntent) -> Unit) {
    Column {
        Surface(Modifier.zIndex(2f), shadowElevation = 5.dp) { TopBar() }
        Surface(Modifier.zIndex(1f).fillMaxSize()) {
            CharacterList(state.characterSimple, onCharacterClick = { onIntent(FavoriteCharactersIntent.OnItemTapped(it)) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = {
            Text(stringResource(R.string.favorite_characters_title))
        },
    )
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
    val state = FavoriteCharactersState(characters.toImmutableList(), status = FavoriteCharactersState.Status(loading = false))
    RickAndMortyTheme { AllCharactersScreen(state) { } }
}
