package cz.frank.rickandmorty.ui.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import cz.frank.rickandmorty.R
import cz.frank.rickandmorty.domain.Character
import cz.frank.rickandmorty.ui.detail.navigation.DetailCharacterNavDestination
import cz.frank.rickandmorty.ui.theme.RickAndMortyTheme
import cz.frank.rickandmorty.utils.ui.ProcessEvents
import cz.frank.rickandmorty.utils.ui.Space
import io.github.fornewid.placeholder.foundation.PlaceholderHighlight
import io.github.fornewid.placeholder.material3.placeholder
import io.github.fornewid.placeholder.material3.shimmer
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.detailCharacterNavDestination(
    navHostController: NavHostController,
) {
    composable<DetailCharacterNavDestination> {
        val arg = it.toRoute<DetailCharacterNavDestination>()
        DetailCharacterRoute(navHostController)
    }
}

@Composable
private fun DetailCharacterRoute(
    navHostController: NavHostController,
    viewModel: DetailCharacterViewModel = koinViewModel()
) {
    viewModel.ProcessEvents {
        when (it) {
            is DetailCharacterEvent.GoBack -> navHostController.navigateUp()
        }
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailCharacterScreen(state, viewModel::onIntent)
}


@Composable
private fun DetailCharacterScreen(state: DetailCharacterState, onIntent: (DetailCharacterIntent) -> Unit) {
    state.character?.let { character ->
        Scaffold(topBar = {
            Surface(Modifier.zIndex(2f), shadowElevation = 5.dp) { TopBar(character.isFavorite, onIntent) }
        }) {
            Surface(
                Modifier
                    .padding(it)
                    .zIndex(1f)
                    .fillMaxSize()) {
                DetailCharacterContent(character)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(isFavorite: Boolean, onIntent: (DetailCharacterIntent) -> Unit) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.favorite_characters_title))
        },
        navigationIcon = {
            IconButton(onClick = { onIntent(DetailCharacterIntent.OnBackTapped) }) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, stringResource(R.string.go_back_description))
            }
        },
        actions = {
            IconButton(onClick = { onIntent(DetailCharacterIntent.OnFavoriteTapped(isFavorite)) }) {
                IconToggleButton(isFavorite, onCheckedChange = { onIntent(DetailCharacterIntent.OnFavoriteTapped(it)) }) {
                    if (isFavorite) {
                        Icon(painterResource(R.drawable.ic_star), stringResource(R.string.detail_character_mark_not_favorite_description))
                    } else {
                        Icon(painterResource(R.drawable.ic_star_empty), stringResource(R.string.detail_character_mark_favorite_description))
                    }
                }
            }
        }
    )
}

@Composable
private fun DetailCharacterContent(character: Character) {
    ElevatedCard(
        Modifier
            .padding(Space.medium)
            .fillMaxSize()
    ) {
        Column {
            ImageWithName(character.imageUrl, character.name, Modifier.padding(Space.medium))
            HorizontalDivider()
            Column(Modifier.fillMaxSize().padding(Space.medium), verticalArrangement = Arrangement.SpaceBetween) {
                HeaderWithValue(R.string.detail_character_status, character.status)
                HeaderWithValue(R.string.detail_character_species, character.species)
                HeaderWithValue(R.string.detail_character_type, character.type)
                HeaderWithValue(R.string.detail_character_gender, character.gender)
                HeaderWithValue(R.string.detail_character_origin, character.origin)
                HeaderWithValue(R.string.detail_character_location, character.location)
            }
        }
    }
}

@Composable
private fun ImageWithName(url: String, name: String, modifier: Modifier = Modifier) {
    var isLoading by remember { mutableStateOf(false) }
    Row(Modifier.fillMaxWidth().then(modifier)) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .listener(
                    onStart = { isLoading = true },
                    onSuccess = { _, _ -> isLoading = false },
                    onError = { _, _ -> isLoading = false }
                )
                .build()
        )

        Image(
            painter,
            contentDescription = name,
            modifier = Modifier
                .size(150.dp)
                .clip(MaterialTheme.shapes.small)
                .placeholder(
                    visible = isLoading,
                    highlight = PlaceholderHighlight.shimmer(),
                ),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.width(Space.medium))
        Column {
            Text(stringResource(R.string.detail_character_name), color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(Space.small))
            Text(name, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun HeaderWithValue(@StringRes header: Int, value: String) {
    Column {
        Text(stringResource(header), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(Space.xsmall))
        Text(value, style = MaterialTheme.typography.labelLarge)
    }
}

@Preview
@Composable
private fun Preview() {
    val character = Character(1, "Rick Sanchez", "Alive", "https://rickandmortyapi.com/api/character/avatar/1.jpeg", true, "Human", "-", "Male", "Earth (C-137)", "Earth (Replacement Dimension)")
    val state = DetailCharacterState(character, status = DetailCharacterState.Status(loading = false))
    RickAndMortyTheme { DetailCharacterScreen(state) { } }
}
