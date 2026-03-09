package be.business.newsapp.feature.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import be.business.newsapp.core.presentation.BaseStateViewModel
import be.business.newsapp.domain.model.Article
import be.business.newsapp.domain.usecase.news.AddToFavouritesUseCase
import be.business.newsapp.domain.usecase.news.GetTopHeadlinesUseCase
import coil.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val imageLoader: ImageLoader,
    val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    val addToFavouritesUseCase: AddToFavouritesUseCase
) : BaseStateViewModel<HomeAction,
        HomeResult,
        HomeEvent,
        HomeState,
        HomeReducer>(initialState = HomeState(), reducer = HomeReducer()) {

    var selectedCategory by mutableStateOf<String?>(null)
        private set
    var selectedCountry by mutableStateOf("us")
        private set

    fun selectCountry(country: String) {
        selectedCountry = country
        action(HomeAction.FetchTopNewsData(country))
    }

    fun selectCategory(category: String?) {
        selectedCategory = category
        action(HomeAction.FetchTopNewsData(selectedCountry, category))
    }

    override fun HomeAction.process(): Flow<HomeResult> = flow {
        when (this@process) {
            is HomeAction.FetchTopNewsData -> emitAll(fetchTopNews(country, category))
            is HomeAction.AddToFavourites -> emitAll(addToFavourites(article))
//            is HomeAction.FetchSportsNews -> emitAll(fetchSportsNews())
//            is HomeAction.FetchWeather -> emitAll(fetchWeather())
        }
    }

    init {
        // Auto fetch when ViewModel starts
        action(HomeAction.FetchTopNewsData(selectedCountry))
    }

    private fun addToFavourites(article: Article): Flow<HomeResult> = flow {

        try {
            val result = addToFavouritesUseCase(params = article)

            result.fold(
                onSuccess = { updatedArticle ->
                    emit(HomeResult.FavouriteUpdated(updatedArticle))

                    emit(
                        HomeEvent.ShowToast(
                            if (updatedArticle.isFavorite)
                                "${article.title} added to Favorites"
                            else
                                "${article.title} removed from Favorites"
                        )
                    )
                },
                onFailure = {
                    emit(HomeResult.Error(it.message ?: "Unknown Error"))
                    emitEvent(HomeEvent.ShowToast("Failed to update favorite"))
                }
            )

        } catch (e: Exception) {
            emit(HomeResult.Error(e.message ?: "Unknown Error"))
        }
    }


    private fun fetchTopNews(country: String, category: String? = null): Flow<HomeResult> = flow {
        emit(HomeResult.Loading)

        try {
            val result = getTopHeadlinesUseCase(
                params = mapOf("country" to country, "category" to (category ?: ""))
            )

            result.fold(
                onSuccess = { emit(HomeResult.TopNewsSuccess(it)) },
                onFailure = { emit(HomeResult.Error(it.message ?: "Unknown Error")) }
            )

        } catch (e: Exception) {
            emit(HomeResult.Error(e.message ?: "Unknown Error"))
        }
    }


}