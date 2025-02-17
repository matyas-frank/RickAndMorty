package cz.frank.rickandmorty.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import cz.frank.rickandmorty.domain.model.Character
import cz.frank.rickandmorty.domain.usecase.DetailCharacterUseCase
import cz.frank.rickandmorty.ui.detail.navigation.DetailCharacterNavDestination
import cz.frank.rickandmorty.utils.ErrorResult
import cz.frank.rickandmorty.utils.ui.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailCharacterViewModel(
    savedStateHandle: SavedStateHandle,
    useCase: DetailCharacterUseCase
)  : BaseViewModel<DetailCharacterState, DetailCharacterIntent, DetailCharacterEvent>() {
    private val status = MutableStateFlow(DetailCharacterState.Status())
    private val character = MutableStateFlow<Character?>(null)

    init {
        val profile = savedStateHandle.toRoute<DetailCharacterNavDestination>()
        viewModelScope.launch {
            useCase(profile.id).onSuccess {
                character.value = it
            }
        }
    }

    override val state: StateFlow<DetailCharacterState> = combine(status, character) { status, characters ->
        DetailCharacterState(characters, status)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailCharacterState(character.value, status.value))

    override suspend fun applyIntent(intent: DetailCharacterIntent) {
        when (intent) {
            is DetailCharacterIntent.OnBackTapped -> emitEvent(DetailCharacterEvent.GoBack)
            is DetailCharacterIntent.OnFavoriteTapped -> onFavoriteTapped(intent.isFavorite)
        }
    }

    private fun onFavoriteTapped(isFavorite: Boolean) {
        character.update { it?.copy(isFavorite = isFavorite) }
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
