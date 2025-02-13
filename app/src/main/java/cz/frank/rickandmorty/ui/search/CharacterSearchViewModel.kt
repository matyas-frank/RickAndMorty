package cz.frank.rickandmorty.ui.search

import androidx.lifecycle.viewModelScope
import cz.frank.rickandmorty.domain.CharacterSimple
import cz.frank.rickandmorty.utils.ErrorResult
import cz.frank.rickandmorty.utils.ui.BaseViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.*

class CharacterSearchViewModel : BaseViewModel<CharacterSearchState, CharacterSearchIntent, CharacterSearchEvent>() {
    private val status = MutableStateFlow(CharacterSearchState.Status())
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
    private val query = MutableStateFlow("")

    override val state: StateFlow<CharacterSearchState> = combine(status, characters, query) { status, characters, query ->
        CharacterSearchState(characters, status, query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CharacterSearchState(characters.value, status.value, query.value))

    override suspend fun applyIntent(intent: CharacterSearchIntent) {
        when (intent) {
            is CharacterSearchIntent.OnItemTapped -> emitEvent(CharacterSearchEvent.GoToDetail(intent.id))
            is CharacterSearchIntent.OnBackTapped -> emitEvent(CharacterSearchEvent.GoBack)
            is CharacterSearchIntent.OnQueryChanged -> query.value = intent.query
            is CharacterSearchIntent.OnClearQueryTapped -> query.value = ""
        }
    }
}

data class CharacterSearchState(
    val characterSimple: ImmutableList<CharacterSimple>,
    val status: Status,
    val query: String,
) {
    data class Status(val loading: Boolean = true, val error: ErrorResult? = null)
}


sealed interface CharacterSearchIntent {
    data class OnItemTapped(val id: Long) : CharacterSearchIntent
    data object OnBackTapped : CharacterSearchIntent
    data class OnQueryChanged(val query: String) : CharacterSearchIntent
    data object OnClearQueryTapped : CharacterSearchIntent
}

sealed interface CharacterSearchEvent {
    data object GoBack : CharacterSearchEvent
    data class GoToDetail(val id: Long) : CharacterSearchEvent
}
