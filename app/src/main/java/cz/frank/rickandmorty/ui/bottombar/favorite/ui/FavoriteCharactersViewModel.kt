package cz.frank.rickandmorty.ui.bottombar.favorite.ui

import cz.frank.rickandmorty.domain.usecase.FavoriteCharactersUseCase
import cz.frank.rickandmorty.utils.ErrorResult
import cz.frank.rickandmorty.utils.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoriteCharactersViewModel(favoriteCharactersUseCase: FavoriteCharactersUseCase) : BaseViewModel<FavoriteCharactersState, FavoriteCharactersIntent, FavoriteCharactersEvent>() {
    private val _state = MutableStateFlow(FavoriteCharactersState())

    val favoriteCharactersFlow = favoriteCharactersUseCase()
    override val state: StateFlow<FavoriteCharactersState> = _state.asStateFlow()

    override suspend fun applyIntent(intent: FavoriteCharactersIntent) {
        when (intent) {
            is FavoriteCharactersIntent.OnItemTapped -> emitEvent(FavoriteCharactersEvent.GoToDetail(intent.id))
        }
    }
}

data class FavoriteCharactersState(val loading: Boolean = true, val error: ErrorResult? = null)


sealed interface FavoriteCharactersIntent {
    data class OnItemTapped(val id: Long) : FavoriteCharactersIntent
}

sealed interface FavoriteCharactersEvent {
    data class GoToDetail(val id: Long) : FavoriteCharactersEvent
}
