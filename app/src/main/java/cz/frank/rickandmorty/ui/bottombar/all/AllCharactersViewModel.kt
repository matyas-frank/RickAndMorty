package cz.frank.rickandmorty.ui.bottombar.all

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import cz.frank.rickandmorty.domain.usecase.AllCharactersUseCase
import cz.frank.rickandmorty.utils.ErrorResult
import cz.frank.rickandmorty.utils.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AllCharactersViewModel(allCharactersFlowUseCase: AllCharactersUseCase) : BaseViewModel<AllCharactersState, AllCharactersIntent, AllCharactersEvent>() {
    private val status = MutableStateFlow(AllCharactersState())
    override val state: StateFlow<AllCharactersState> = status.asStateFlow()

    val allCharactersFlow = allCharactersFlowUseCase().cachedIn(viewModelScope)

    override suspend fun applyIntent(intent: AllCharactersIntent) {
        when (intent) {
            is AllCharactersIntent.OnSearchTapped -> emitEvent(AllCharactersEvent.GoToQuerySearch)
            is AllCharactersIntent.OnItemTapped -> emitEvent(AllCharactersEvent.GoToDetail(intent.id))
            is AllCharactersIntent.OnBackTapped -> emitEvent(AllCharactersEvent.GoBack)
        }
    }
}

data class AllCharactersState(val loading: Boolean = true, val error: ErrorResult? = null)


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
