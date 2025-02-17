package cz.frank.rickandmorty.ui.search

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import cz.frank.rickandmorty.domain.usecase.QueryCharactersUseCase
import cz.frank.rickandmorty.domain.usecase.QueryCharactersUseCaseParams
import cz.frank.rickandmorty.utils.ErrorResult
import cz.frank.rickandmorty.utils.ui.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.milliseconds

class QuerySearchCharactersViewModel(querySearchedCharactersUseCase: QueryCharactersUseCase) : BaseViewModel<QuerySearchCharactersState, QuerySearchCharactersIntent, QuerySearchCharactersEvent>() {
    private val status = MutableStateFlow(QuerySearchCharactersState.Status())
    private val query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val charactersFlow = query.debounce(400.milliseconds).flatMapLatest { querySearchedCharactersUseCase(QueryCharactersUseCaseParams(it, viewModelScope)) }.cachedIn(viewModelScope)

    override val state: StateFlow<QuerySearchCharactersState> = combine(status, query) { status, query ->
        QuerySearchCharactersState(status, query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuerySearchCharactersState(status.value, query.value))

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
