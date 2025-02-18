package cz.frank.rickandmorty.ui.bottombar.favorite.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import cz.frank.rickandmorty.domain.usecase.FavoriteCharactersUseCase
import cz.frank.rickandmorty.utils.ui.BaseViewModel
import kotlinx.coroutines.flow.*

class FavoriteCharactersViewModel(favoriteCharactersUseCase: FavoriteCharactersUseCase) : BaseViewModel<FavoriteCharactersIntent, FavoriteCharactersEvent>() {
    private val _state = MutableStateFlow(FavoriteCharactersState())

    val favoriteCharactersFlow = favoriteCharactersUseCase()
        .cachedIn(viewModelScope)
        .onStart { _state.update { it.copy(loading = true) } }
        .onEach { _state.update { it.copy(loading = false) } }

    val state: StateFlow<FavoriteCharactersState> = _state.asStateFlow()

    override suspend fun applyIntent(intent: FavoriteCharactersIntent) {
        when (intent) {
            is FavoriteCharactersIntent.OnItemTapped -> emitEvent(FavoriteCharactersEvent.GoToDetail(intent.id))
        }
    }
}

data class FavoriteCharactersState(val loading: Boolean = true)

sealed interface FavoriteCharactersIntent {
    data class OnItemTapped(val id: Long) : FavoriteCharactersIntent
}

sealed interface FavoriteCharactersEvent {
    data class GoToDetail(val id: Long) : FavoriteCharactersEvent
}
