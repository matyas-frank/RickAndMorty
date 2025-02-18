package cz.frank.rickandmorty.ui.bottombar.all

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import cz.frank.rickandmorty.domain.usecase.AllCharactersUseCase
import cz.frank.rickandmorty.utils.ui.BaseViewModel

class AllCharactersViewModel(allCharactersFlowUseCase: AllCharactersUseCase) : BaseViewModel<AllCharactersIntent, AllCharactersEvent>() {
    val allCharactersFlow = allCharactersFlowUseCase().cachedIn(viewModelScope)

    override suspend fun applyIntent(intent: AllCharactersIntent) {
        when (intent) {
            is AllCharactersIntent.OnSearchTapped -> emitEvent(AllCharactersEvent.GoToQuerySearch)
            is AllCharactersIntent.OnItemTapped -> emitEvent(AllCharactersEvent.GoToDetail(intent.id))
            is AllCharactersIntent.OnBackTapped -> emitEvent(AllCharactersEvent.GoBack)
            is AllCharactersIntent.OnRefreshRetryTapped -> emitEvent(AllCharactersEvent.RetryRequest)
            is AllCharactersIntent.OnAppendRetryTapped -> emitEvent(AllCharactersEvent.RetryRequest)
        }
    }
}

sealed interface AllCharactersIntent {
    data object OnSearchTapped : AllCharactersIntent
    data class OnItemTapped(val id: Long) : AllCharactersIntent
    data object OnBackTapped : AllCharactersIntent
    data object OnRefreshRetryTapped : AllCharactersIntent
    data object OnAppendRetryTapped : AllCharactersIntent
}

sealed interface AllCharactersEvent {
    data object GoBack : AllCharactersEvent
    data class GoToDetail(val id: Long) : AllCharactersEvent
    data object GoToQuerySearch : AllCharactersEvent
    data object RetryRequest : AllCharactersEvent
}
