package be.business.newsapp.core.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.StateFlow

/**
 * Observe [BaseStateViewModel.event] in a Compose [LaunchedEffect].
 * @param lifecycleState [Lifecycle.State] in which [lifecycleState] block runs.
 */
@SuppressLint("ComposableNaming")
@Composable
fun <CustomAction : MviAction, CustomResult : MviResult, CustomEvent : MviEvent,
        CustomState : MviViewState, CustomReducer : MviStateReducer<CustomState, CustomResult>>
        BaseStateViewModel<CustomAction, CustomResult, CustomEvent,
                CustomState, CustomReducer>.collectEvents(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    eventHandler: (suspend (event: CustomEvent) -> Unit)
) {
    val eventFlow = this.event
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    LaunchedEffect(eventFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            eventFlow.collect {
                eventHandler(it)
            }
        }
    }
}

/**
 * Observe [BaseStateViewModel.state] as [State].
 * @param lifecycleState The Lifecycle where the restarting collecting from this flow work will be kept alive.
 */
@Composable
fun <CustomAction : MviAction, CustomResult : MviResult, CustomEvent : MviEvent,
        CustomState : MviViewState, CustomReducer : MviStateReducer<CustomState, CustomResult>>
        BaseStateViewModel<CustomAction, CustomResult, CustomEvent,
                CustomState, CustomReducer>.collectState(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
): State<CustomState> {
    return this.state.collectState(lifecycleState = lifecycleState)
}

@Composable
private fun <T> StateFlow<T>.collectState(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
): State<T> {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val stateFlowLifecycleAware = remember(this, lifecycleOwner) {
        this.flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState)
    }

    // Need to access the initial value to convert to State - collectAsState() suppresses this lint warning too
    @SuppressLint("StateFlowValueCalledInComposition")
    val initialValue = this.value
    return stateFlowLifecycleAware.collectAsState(initialValue)
}


/**
 * Observe [BaseViewModel.event] in a Compose [LaunchedEffect].
 * @param lifecycleState [Lifecycle.State] in which [lifecycleState] block runs.
 */
@SuppressLint("ComposableNaming")
@Composable
fun <A : MviAction, S : MviViewState, E : MviEvent> BaseViewModel<A, S, E>.collectEvents(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    eventHandler: (suspend (event: E) -> Unit)
) {
    val eventFlow = this.event
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    LaunchedEffect(eventFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            eventFlow.collect {
                eventHandler(it)
            }
        }
    }
}

/**
 * Observe [BaseViewModel.state] as [State].
 * @param lifecycleState The Lifecycle where the restarting collecting from this flow work will be kept alive.
 */
@Composable
fun <A : MviAction, S : MviViewState, E : MviEvent> BaseViewModel<A, S, E>.collectState(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
): State<S> {
    return this.state.collectState(lifecycleState = lifecycleState)
}