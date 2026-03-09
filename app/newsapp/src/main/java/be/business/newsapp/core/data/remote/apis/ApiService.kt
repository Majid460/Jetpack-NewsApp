package be.business.newsapp.core.data.remote.apis

import be.business.newsapp.domain.model.newsresponse.NewsResponseDto

interface ApiService {
    // Top Headlines based on country
    suspend fun getTopHeadlinesOfCountry(country: String,category: String? = null): Result<NewsResponseDto>

    // Top Headlines based on channels
    suspend fun getTopHeadlinesOfChannel(sources: String): Result<NewsResponseDto>
}