package be.business.newsapp.feature.favorite

import be.business.newsapp.core.presentation.MviAction
import be.business.newsapp.core.presentation.MviEvent
import be.business.newsapp.core.presentation.MviResult
import be.business.newsapp.core.presentation.MviStateReducer
import be.business.newsapp.core.presentation.MviViewState
import be.business.newsapp.domain.model.Article

sealed class FavoriteAction : MviAction {
    data object FetchFavNewsData : FavoriteAction()
    //  data class AddToFavourites(val article: Article) : FavoriteAction()
}

sealed class FavoriteEvent : MviEvent, FavoriteResult() {
    data object NavigateToDetails : FavoriteEvent()
    data class ShowToast(val message: String) : FavoriteEvent()
}

sealed class FavoriteResult : MviResult {
    object Loading : FavoriteResult()
    data class Success(val data: List<Article>) : FavoriteResult()
    data class Error(val message: String) : FavoriteResult()
}

sealed class FavoriteState : MviViewState {
    data object Initial : FavoriteState()
    data object Loading : FavoriteState()
    data class Success(val data: List<Article>) : FavoriteState()
    data class Error(val message: String) : FavoriteState()

}

class FavoriteReducer : MviStateReducer<FavoriteState, FavoriteResult> {
    override fun FavoriteState.reduce(result: FavoriteResult): FavoriteState {
        return when (result) {
            is FavoriteResult.Loading -> FavoriteState.Loading
            is FavoriteResult.Success -> FavoriteState.Success(result.data)
            is FavoriteResult.Error -> FavoriteState.Error(result.message)
            else -> {}
        } as FavoriteState
    }

}