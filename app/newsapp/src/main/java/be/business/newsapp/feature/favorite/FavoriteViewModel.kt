package be.business.newsapp.feature.favorite

import be.business.newsapp.core.presentation.BaseStateViewModel
import be.business.newsapp.domain.usecase.news.GetFavouriteNewsUseCase
import coil.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    val getFavouriteNewsUseCase: GetFavouriteNewsUseCase,
    val imageLoader: ImageLoader
) :
    BaseStateViewModel<FavoriteAction,
            FavoriteResult,
            FavoriteEvent,
            FavoriteState,
            FavoriteReducer>(initialState = FavoriteState.Initial, reducer = FavoriteReducer()) {


    init {
        action(FavoriteAction.FetchFavNewsData)
    }
    override fun FavoriteAction.process(): Flow<FavoriteResult> {
        return when (this) {
            is FavoriteAction.FetchFavNewsData -> getFavouriteNews()
        }
    }
    fun getFavouriteNews(): Flow<FavoriteResult> =
        getFavouriteNewsUseCase(Unit)
            .map { result ->
                result.fold(
                    onSuccess = { FavoriteResult.Success(it) },
                    onFailure = { FavoriteResult.Error(it.message ?: "Unknown Error") }
                )
            }
            .onStart { emit(FavoriteResult.Loading) }


}