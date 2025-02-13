package cz.frank.rickandmorty.ui.detail

import androidx.lifecycle.viewModelScope
import cz.frank.rickandmorty.domain.model.Character
import cz.frank.rickandmorty.utils.ErrorResult
import cz.frank.rickandmorty.utils.ui.BaseViewModel
import kotlinx.coroutines.flow.*

class DetailCharacterViewModel  : BaseViewModel<DetailCharacterState, DetailCharacterIntent, DetailCharacterEvent>() {
    private val status = MutableStateFlow(DetailCharacterState.Status())
    private val character = MutableStateFlow<Character?>(Character(1, "Rick Sanchez", "Alive", "https://rickandmortyapi.com/api/character/avatar/1.jpeg", true, "Human", "-", "Male", "Earth (C-137)", "Earth (Replacement Dimension)"))

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
