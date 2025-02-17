package cz.frank.rickandmorty.ui.bottombar.favorite.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.utils.ErrorResult
import cz.frank.rickandmorty.utils.ui.BaseViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.*

class FavoriteCharactersViewModel : BaseViewModel<FavoriteCharactersState, FavoriteCharactersIntent, FavoriteCharactersEvent>() {
    private val status = MutableStateFlow(FavoriteCharactersState.Status())
    private val characters = MutableStateFlow(
        persistentListOf(
            CharacterSimple(1, "Rick Sanchez", "Alive", "https://rickandmortyapi.com/api/character/avatar/1.jpeg", true),
            CharacterSimple(3, "Morty Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/2.jpeg", true),
            CharacterSimple(4, "Summer Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/3.jpeg", true),
        )
    )

    val favoriteCharactersFlow = flowOf(PagingData.from(characters.value))

    override val state: StateFlow<FavoriteCharactersState> = combine(status, characters) { status, characters ->
        FavoriteCharactersState(characters, status)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FavoriteCharactersState(characters.value, status.value))

    override suspend fun applyIntent(intent: FavoriteCharactersIntent) {
        when (intent) {
            is FavoriteCharactersIntent.OnItemTapped -> emitEvent(FavoriteCharactersEvent.GoToDetail(intent.id))
        }
    }
}

data class FavoriteCharactersState(val characterSimple: ImmutableList<CharacterSimple>, val status: Status = Status()) {
    data class Status(val loading: Boolean = true, val error: ErrorResult? = null)
}


sealed interface FavoriteCharactersIntent {
    data class OnItemTapped(val id: Long) : FavoriteCharactersIntent
}

sealed interface FavoriteCharactersEvent {
    data class GoToDetail(val id: Long) : FavoriteCharactersEvent
}
