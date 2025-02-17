package cz.frank.rickandmorty.ui.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import cz.frank.rickandmorty.domain.model.CharacterSimple
import cz.frank.rickandmorty.utils.ErrorResult
import cz.frank.rickandmorty.utils.ui.BaseViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.*

class QuerySearchCharactersViewModel : BaseViewModel<QuerySearchCharactersState, QuerySearchCharactersIntent, QuerySearchCharactersEvent>() {
    private val status = MutableStateFlow(QuerySearchCharactersState.Status())
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

    val charactersFlow = flowOf(PagingData.from(characters.value))

    override val state: StateFlow<QuerySearchCharactersState> = combine(status, characters, query) { status, characters, query ->
        QuerySearchCharactersState(characters, status, query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuerySearchCharactersState(characters.value, status.value, query.value))

    override suspend fun applyIntent(intent: QuerySearchCharactersIntent) {
        when (intent) {
            is QuerySearchCharactersIntent.OnItemTapped -> emitEvent(QuerySearchCharactersEvent.GoToDetail(intent.id))
            is QuerySearchCharactersIntent.OnBackTapped -> emitEvent(QuerySearchCharactersEvent.GoBack)
            is QuerySearchCharactersIntent.OnQueryChanged -> query.value = intent.query
            is QuerySearchCharactersIntent.OnClearQueryTapped -> query.value = ""
        }
    }
}

data class QuerySearchCharactersState(
    val characterSimple: ImmutableList<CharacterSimple>,
    val status: Status,
    val query: String,
) {
    data class Status(val loading: Boolean = true, val error: ErrorResult? = null)
}


sealed interface QuerySearchCharactersIntent {
    data class OnItemTapped(val id: Long) : QuerySearchCharactersIntent
    data object OnBackTapped : QuerySearchCharactersIntent
    data class OnQueryChanged(val query: String) : QuerySearchCharactersIntent
    data object OnClearQueryTapped : QuerySearchCharactersIntent
}

sealed interface QuerySearchCharactersEvent {
    data object GoBack : QuerySearchCharactersEvent
    data class GoToDetail(val id: Long) : QuerySearchCharactersEvent
}
