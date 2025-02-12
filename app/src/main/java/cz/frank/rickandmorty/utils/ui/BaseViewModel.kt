package cz.frank.rickandmorty.utils.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Intent, Event> : ViewModel() {
    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()
    protected fun emitEvent(event: Event) { viewModelScope.launch { _events.emit(event) } }

    abstract val state: StateFlow<State>

    abstract suspend fun applyIntent(intent: Intent)
    fun onIntent(intent: Intent) {
        viewModelScope.launch { applyIntent(intent) }
    }
}


@Composable
fun <E> BaseViewModel<*, *, E>.ProcessEvents(onEvent: (E) -> Unit){
    val lifecycle = LocalLifecycleOwner.current
    LaunchedEffect(this) {
        events.flowWithLifecycle(lifecycle.lifecycle).collect { onEvent(it) }
    }
}
