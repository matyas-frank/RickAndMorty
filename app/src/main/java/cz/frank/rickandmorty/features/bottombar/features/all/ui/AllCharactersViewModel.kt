package cz.frank.rickandmorty.features.bottombar.features.all.ui

import androidx.lifecycle.viewModelScope
import cz.frank.rickandmorty.root.domain.CharacterSimple
import cz.frank.rickandmorty.utils.ErrorResult
import cz.frank.rickandmorty.utils.ui.BaseViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.*

class AllCharactersViewModel : BaseViewModel<State, AllCharactersIntent, AllCharactersEvent>() {
    private val status = MutableStateFlow(State.Status())
    private val characters = MutableStateFlow(
        persistentListOf(
            CharacterSimple(1, "Rick Sanchez", "Alive", "https://rickandmortyapi.com/api/character/avatar/1.jpeg", true),
            CharacterSimple(3, "Morty Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/2.jpeg", true),
            CharacterSimple(4, "Summer Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/3.jpeg", true),
            CharacterSimple(5, "Beth Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/4.jpeg"),
            CharacterSimple(6, "Jerry Smith", "Alive", "https://rickandmortyapi.com/api/character/avatar/5.jpeg"),
            CharacterSimple(7, "Eric Stoltz Mask Morty", "Alive", "https://rickandmortyapi.com/api/character/avatar/6.jpeg"),
            CharacterSimple(8, "Abradolf Lincler", "Unknown", "https://rickandmortyapi.com/api/character/avatar/7.jpeg")
        )
    )

    override val state: StateFlow<State> = combine(status, characters) { status, characters ->
        State(characters, status)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), State(characters.value, status.value))

    override suspend fun applyIntent(intent: AllCharactersIntent) {
        when (intent) {
            is AllCharactersIntent.OnSearchTapped -> emitEvent(AllCharactersEvent.GoToQuerySearch)
            is AllCharactersIntent.OnItemTapped -> emitEvent(AllCharactersEvent.GoToDetail(intent.id))
            is AllCharactersIntent.OnBackTapped -> emitEvent(AllCharactersEvent.GoBack)
        }
    }
}

data class State(val characterSimple: ImmutableList<CharacterSimple>, val status: Status = Status()) {
    data class Status(val loading: Boolean = true, val error: ErrorResult? = null)
}


sealed interface AllCharactersIntent {
    data object OnSearchTapped : AllCharactersIntent
    data class OnItemTapped(val id: Long) : AllCharactersIntent
    data object OnBackTapped : AllCharactersIntent
}

sealed interface AllCharactersEvent {
    data object GoBack : AllCharactersEvent
    data class GoToDetail(val id: Long) : AllCharactersEvent
    data object GoToQuerySearch : AllCharactersEvent
}
