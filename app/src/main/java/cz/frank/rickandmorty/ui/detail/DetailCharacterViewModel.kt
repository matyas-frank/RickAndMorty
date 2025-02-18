package cz.frank.rickandmorty.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import cz.frank.rickandmorty.domain.model.Character
import cz.frank.rickandmorty.domain.usecase.ChangeFavoriteStatusUseCase
import cz.frank.rickandmorty.domain.usecase.ChangeFavoriteStatusUseCaseParams
import cz.frank.rickandmorty.domain.usecase.GetDetailCharacterUseCase
import cz.frank.rickandmorty.ui.detail.navigation.DetailCharacterNavDestination
import cz.frank.rickandmorty.utils.ErrorResult
import cz.frank.rickandmorty.utils.ui.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailCharacterViewModel(
    savedStateHandle: SavedStateHandle,
    getDetailCharacterUseCase: GetDetailCharacterUseCase,
    private val changeFavoriteStatusUseCase: ChangeFavoriteStatusUseCase,
) : BaseViewModel<DetailCharacterIntent, DetailCharacterEvent>() {
    private val status = MutableStateFlow(DetailCharacterState.Status())

    private val characterId = savedStateHandle.toRoute<DetailCharacterNavDestination>().id

    private val character = getDetailCharacterUseCase(characterId).map {
        it.fold(
            onSuccess = { it },
            onFailure = { null }
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val state: StateFlow<DetailCharacterState> = combine(status, character) { status, character ->
        DetailCharacterState(character, status)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailCharacterState(character.value, status.value))

    override suspend fun applyIntent(intent: DetailCharacterIntent) {
        when (intent) {
            is DetailCharacterIntent.OnBackTapped -> emitEvent(DetailCharacterEvent.GoBack)
            is DetailCharacterIntent.OnFavoriteTapped -> onFavoriteTapped(intent.isFavorite)
        }
    }

    private fun onFavoriteTapped(isFavorite: Boolean) {
        viewModelScope.launch { changeFavoriteStatusUseCase(ChangeFavoriteStatusUseCaseParams(characterId, isFavorite)) }
    }
}

data class DetailCharacterState(val character: Character?, val status: Status = Status()) {
    data class Status(val loading: Boolean = true, val error: ErrorResult? = null)
}


sealed interface DetailCharacterIntent {
    data object OnBackTapped: DetailCharacterIntent
    data class OnFavoriteTapped(val isFavorite: Boolean) : DetailCharacterIntent
}

sealed interface DetailCharacterEvent {
    data object GoBack : DetailCharacterEvent
}
