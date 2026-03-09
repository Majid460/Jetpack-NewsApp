package be.business.newsapp.core.data.repository

import be.business.newsapp.core.data.local.datastore.DataManager
import be.business.newsapp.core.data.local.room.fav.FavoriteArticleEntity
import be.business.newsapp.core.data.remote.apis.ApiService
import be.business.newsapp.domain.model.newsresponse.NewsResponseDto
import be.business.newsapp.domain.repository.NewsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dataManager: DataManager
) : NewsRepository {
    override suspend fun getTopHeadlines(country: String, category: String?): Result<NewsResponseDto> {
        return apiService.getTopHeadlinesOfCountry(country,category)
    }

    override suspend fun getTopHeadlinesOfChannels(sources: String): Result<NewsResponseDto> {
        return apiService.getTopHeadlinesOfChannel(sources)
    }

    override suspend fun addArticleToFavorites(article: FavoriteArticleEntity) =
        dataManager.database.favNewDao().addFavorite(article)

    override suspend fun removeArticleFromFavorites(article: FavoriteArticleEntity) =
        dataManager.database.favNewDao().removeFavorite(article.url)

    override fun getFavorites(): Flow<List<FavoriteArticleEntity>> {
        return dataManager.database.favNewDao().getFavorites()
    }

    override suspend fun getFavoritesUrls(): Set<String> {
        return dataManager.database.favNewDao().getFavoriteArticlesOnce().map { it.url }.toSet()
    }

    override suspend fun isFavorite(url: String): Boolean {
        return dataManager.database.favNewDao().isFavorite(url)
    }
}