package cz.frank.rickandmorty.utils.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import cz.frank.rickandmorty.R
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.ui.theme.RickAndMortyTheme
import io.github.fornewid.placeholder.foundation.PlaceholderHighlight
import io.github.fornewid.placeholder.material3.placeholder
import io.github.fornewid.placeholder.material3.shimmer
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flowOf

@Composable
fun CharacterList(
    characters: LazyPagingItems<CharacterSimple>,
    modifier: Modifier = Modifier,
    areCharacterCardsTransparent: Boolean = false,
    onRetryAppend: () -> Unit,
    onCharacterClick: (Long) -> Unit,
) {
    LazyColumn(modifier) {
        items(characters.itemCount) { index ->
            val item = characters[index]
            if (item != null) {
                CharacterItem(item, areCharacterCardsTransparent, onCharacterClick)
            }
        }
        if (characters.loadState.append is LoadState.Error) {
            item {
                Button(onRetryAppend, Modifier.padding(Space.small).fillMaxWidth(), shape = MaterialTheme.shapes.small) {
                    Text(stringResource(R.string.character_list_retry_append))
                }
            }
        }
    }
}

@Composable
private fun CharacterItem(character: CharacterSimple, isTransparent: Boolean, onCharacterClick: (Long) -> Unit) {
    ElevatedCard(
        onClick = { onCharacterClick(character.id) },
        Modifier.padding(horizontal =  Space.medium, vertical = Space.small),
        colors = CardDefaults.elevatedCardColors(containerColor = if (isTransparent) Color.Transparent else Color.Unspecified),
        elevation = if (isTransparent) CardDefaults.elevatedCardElevation(0.dp) else CardDefaults.elevatedCardElevation(),
        shape = MaterialTheme.shapes.large,
    ) {
        var isLoading by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Space.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.imageUrl)
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
                contentDescription = character.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(MaterialTheme.shapes.small)
                    .placeholder(
                        visible = isLoading,
                        highlight = PlaceholderHighlight.shimmer(),
                    ),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(Space.mediumSmall))

            Column {
                Row {
                    val style = MaterialTheme.typography.labelLarge
                    Text(
                        character.name,
                        modifier = Modifier.weight(1f, fill = false),
                        style = style
                    )
                    Spacer(Modifier.width(Space.small))
                    if (character.isFavorite) {
                        Icon(
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(with(LocalDensity.current) { style.fontSize.toDp() })
                        )
                    }
                }
                Text(character.status, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Preview
@Composable
private fun CharacterListSearchPreview(@PreviewParameter(PreviewProvider::class) state: CharactersStyleState) {
    val characters = listOf(
        CharacterSimple(1,"Rick Sanchez", "Alive", "https://rickandmortyapi.com/api/character/avatar/1.jpeg", true),
        CharacterSimple(3,"Morty Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/2.jpeg", true),
        CharacterSimple(4,"Summer Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/3.jpeg", true),
        CharacterSimple(5,"Beth Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/4.jpeg"),
        CharacterSimple(6,"Jerry Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/5.jpeg"),
        CharacterSimple(7,"Eric Stoltz Mask Morty", "Alive", "https://rickandmortyapi.com/api/character/avatar/6.jpeg"),
        CharacterSimple(8,"Abradolf Lincler", "Unknown", "https://rickandmortyapi.com/api/character/avatar/7.jpeg")
    )
    RickAndMortyTheme(state.isDarkTheme) {
        Scaffold {
            CharacterList(
                flowOf(PagingData.from(characters.toImmutableList())).collectAsLazyPagingItems(),
                Modifier.padding(it),
                areCharacterCardsTransparent = state.areCharacterCardsTransparent,
                onRetryAppend = {}
            ) {}
        }
    }
}

private data class CharactersStyleState(val isDarkTheme: Boolean, val areCharacterCardsTransparent: Boolean)

private class PreviewProvider: PreviewParameterProvider<CharactersStyleState> {
    override val values: Sequence<CharactersStyleState> = sequenceOf(
        CharactersStyleState(isDarkTheme = false,  areCharacterCardsTransparent = false),
        CharactersStyleState(isDarkTheme = false,  areCharacterCardsTransparent = true),
        CharactersStyleState(isDarkTheme = true,  areCharacterCardsTransparent = false),
        CharactersStyleState(isDarkTheme = true,  areCharacterCardsTransparent = true),
    )
}
