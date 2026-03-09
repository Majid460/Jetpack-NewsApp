package be.business.newsapp.core.presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * A simpler version of [BaseStateViewModel] without a [MviStateReducer] and [MviResult].
 * It allows a free control over the [MviViewState] and [MviEvent]
 */
abstract class BaseViewModel<Action : MviAction, State : MviViewState, Event : MviEvent>(
    initialState: State,
//    private val connectivityManagerService: ConnectivityManagerService
) : ViewModel() {

    private val _fsmFlow = MutableSharedFlow<Action>(
        extraBufferCapacity = 20,
        onBufferOverflow = BufferOverflow.DROP_OLDEST // gotta use this in order to be able to use tryEmit function
    )

    private val _event = MutableSharedFlow<Event>(
        extraBufferCapacity = 20,
        onBufferOverflow = BufferOverflow.DROP_OLDEST // gotta use this in order to be able to use tryEmit function
    )
    val event: SharedFlow<Event> = _event

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state

    val _connectivity = MutableStateFlow(true)
    val connectivity: StateFlow<Boolean> = _connectivity

    init {
        setupStateMachine()
    }

//    private fun observeConnectivity() {
//        viewModelScope.launch {
//            connectivityManagerService.listenToConnectivity()
//                .onEach { isConnected ->
//                    _connectivity.value = isConnected
//                    updateStateWithConnectivity(isConnected)
//                }
//                .launchIn(this)
//        }
//    }

//    private fun updateStateWithConnectivity(isConnected: Boolean) {
//        _connectivity.value = isConnected
//    }

    private fun setupStateMachine() {
        _fsmFlow.onEach {
            it.process()
        }.launchIn(viewModelScope)
    }

    fun emitEvent(event: Event) {
        _event.tryEmit(event)
    }

    fun updateState(state: State) {
        _state.value = state
    }

    fun action(action: Action) {
        _fsmFlow.tryEmit(action)
    }

    protected abstract fun Action.process()
}