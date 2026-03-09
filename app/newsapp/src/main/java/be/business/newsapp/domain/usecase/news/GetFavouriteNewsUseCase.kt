package be.business.newsapp.domain.usecase.news

import be.business.newsapp.domain.model.Article
import be.business.newsapp.domain.repository.NewsRepository
import be.business.newsapp.domain.usecase.ObservableUseCaseWithResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetFavouriteNewsUseCase : ObservableUseCaseWithResult<List<Article>, Unit>

class GetFavouriteNewsUseCaseImpl @Inject constructor(val repository: NewsRepository) :
    GetFavouriteNewsUseCase {

    override fun invoke(params: Unit): Flow<Result<List<Article>>> =
        repository.getFavorites()
            .map { favEntities ->
                runCatching { favEntities.map { it.toDomain() } }
            }

}