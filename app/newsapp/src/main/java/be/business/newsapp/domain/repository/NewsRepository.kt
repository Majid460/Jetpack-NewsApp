package be.business.newsapp.domain.repository

import be.business.newsapp.core.data.local.room.fav.FavoriteArticleEntity
import be.business.newsapp.domain.model.newsresponse.ArticleDto
import be.business.newsapp.domain.model.newsresponse.NewsResponseDto
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getTopHeadlines(country: String,category: String? = null): Result<NewsResponseDto>
    suspend fun getTopHeadlinesOfChannels(sources: String): Result<NewsResponseDto>

    // Local DB functions
    suspend fun addArticleToFavorites(article: FavoriteArticleEntity)
    suspend fun removeArticleFromFavorites(article: FavoriteArticleEntity)
    fun getFavorites(): Flow<List<FavoriteArticleEntity>>
    suspend fun getFavoritesUrls(): Set<String>
    suspend fun isFavorite(url: String): Boolean

}
