package be.business.newsapp.feature.home.presentation

import be.business.newsapp.core.presentation.MviAction
import be.business.newsapp.core.presentation.MviEvent
import be.business.newsapp.core.presentation.MviResult
import be.business.newsapp.core.presentation.MviStateReducer
import be.business.newsapp.core.presentation.MviViewState
import be.business.newsapp.domain.model.Article
import be.business.newsapp.domain.model.NewsResponse

sealed class HomeAction : MviAction {
    data class FetchTopNewsData(val country: String, val category: String? = null) : HomeAction()
    data class AddToFavourites(val article: Article) : HomeAction()

}
sealed class HomeEvent : MviEvent, HomeResult() {
    data object NavigateToDetails : HomeEvent()
    data class ShowToast(val message: String) : HomeEvent()
}
// For Reducer view model
// Action -> Result -> State
// Action
// ⬇
//process() → Flow<Result>
// ⬇
//scan(previousState, result)
// ⬇
//New State
//sealed class HomeResult : MviResult {
//    data object Loading : HomeResult()
//    data class Success(val data: NewsResponse) : HomeResult()
//    data class Error(val message: String) : HomeResult()
//}
 sealed class HomeResult : MviResult {
    object Loading : HomeResult()
    data class TopNewsSuccess(val data: NewsResponse) : HomeResult()

    data class FavouriteUpdated(val article: Article) : HomeResult()
    data class SportsSuccess(val data: NewsResponse) : HomeResult()
   // data class WeatherSuccess(val data: WeatherResponse) : HomeResult()
    data class Error(val message: String) : HomeResult()
}
//sealed class HomeState : MviViewState {
//    data object Initial : HomeState()
//    data object Loading : HomeState()
//    data class Success(val data: NewsResponse) : HomeState()
//    data class Error(val message: String) : HomeState()
//}

// For handling multiple states
 data class HomeState(
    val loading: Boolean = false,
    val topNews: NewsResponse? = null,
    val sportsNews: NewsResponse? = null,
    //val weather: WeatherResponse? = null,
    val error: String? = null
) : MviViewState

//class HomeReducer : MviStateReducer<HomeState, HomeResult> {
//    override fun HomeState.reduce(result: HomeResult): HomeState {
//        return when (result) {
//            is HomeResult.Loading -> HomeState.Loading
//
//            is HomeResult.Success -> HomeState.Success(result.data)
//
//            is HomeResult.Error -> HomeState.Error(result.message)
//        }
//    }
//}

// New reducer for the multiple states
 class HomeReducer : MviStateReducer<HomeState, HomeResult> {
    override fun HomeState.reduce(result: HomeResult): HomeState {
        return when(result) {
            is HomeResult.Loading -> copy(loading = true)
            is HomeResult.TopNewsSuccess -> copy(topNews = result.data, loading = false)
            is HomeResult.SportsSuccess -> copy(sportsNews = result.data, loading = false)
            is HomeResult.FavouriteUpdated -> copy(
                topNews = topNews?.copy(
                    article = topNews.article.map {
                        if (it.url == result.article.url) result.article else it
                    }
                ),
                loading = false
            )
            // is HomeResult.WeatherSuccess -> copy(weather = result.data, loading = false)
            is HomeResult.Error -> copy(error = result.message, loading = false)
            else -> {}
        } as HomeState
    }
}