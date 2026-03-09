package be.business.newsapp.domain.usecase.news

import be.business.newsapp.domain.model.Article
import be.business.newsapp.domain.repository.NewsRepository
import be.business.newsapp.domain.usecase.SuspendableUseCase

interface AddToFavouritesUseCase : SuspendableUseCase<Article, Article>

class AddToFavouritesUseCaseImpl(
    private val repository: NewsRepository
) : AddToFavouritesUseCase {

    override suspend fun invoke(params: Article): Result<Article> {
        return try {
            val isAlreadyFav = repository.isFavorite(params.url ?: "")

            // Toggle in memory
            val updated = params.copy(isFavorite = !isAlreadyFav)

            if (isAlreadyFav) {
                // Remove
                repository.removeArticleFromFavorites(updated.toEntity())
            } else {
                // Add
                repository.addArticleToFavorites(updated.toEntity())
            }

            Result.success(updated)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


