package cz.frank.rickandmorty.utils.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Intent, Event> : ViewModel() {
    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()
    fun emitEvent(event: Event) { viewModelScope.launch { _events.emit(event) } }

    abstract val state: StateFlow<State>
    abstract fun onIntent(intent: Intent)

    abstract suspend fun applyIntent(intent: Intent)
    fun onIntent(intent: Intent) {
        viewModelScope.launch { applyIntent(intent) }
    }
}

}
