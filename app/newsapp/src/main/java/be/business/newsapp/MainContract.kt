package be.business.newsapp

import be.business.newsapp.core.presentation.MviAction
import be.business.newsapp.core.presentation.MviEvent
import be.business.newsapp.core.presentation.MviViewState

sealed class MainAction : MviAction {
    data object Started : MainAction()
    data class ChangeTheme(val theme:Boolean) : MainAction()
}
sealed class MainEvent : MviEvent {
    data object ShowToast : MainEvent()
}

sealed class MainState : MviViewState {
    data object Initial : MainState()
}