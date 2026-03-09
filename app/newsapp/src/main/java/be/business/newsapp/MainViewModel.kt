package be.business.newsapp

import androidx.lifecycle.viewModelScope
import be.business.newsapp.core.data.local.datastore.DataManager
import be.business.newsapp.core.presentation.AppState
import be.business.newsapp.core.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val dataManager: DataManager) :
    BaseViewModel<MainAction, MainState, MainEvent>(MainState.Initial) {

    override fun MainAction.process() {
       when(this){
           is MainAction.ChangeTheme -> {
               onChangeTheme(theme)
           }
           else -> {}
       }
    }
    private fun onChangeTheme(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            AppState.updateTheme(value)
            dataManager.preferences.isDarkTheme(value)
        }
    }
    fun checkTheme() = viewModelScope.launch(Dispatchers.IO) {
        AppState.updateTheme(dataManager.preferences.isDarkTheme().first())
    }

}