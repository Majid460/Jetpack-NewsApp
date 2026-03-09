package be.business.newsapp.domain.usecase.news

import be.business.newsapp.domain.model.NewsResponse
import be.business.newsapp.domain.model.newsresponse.NewsResponseDto
import be.business.newsapp.domain.repository.NewsRepository
import be.business.newsapp.domain.usecase.SuspendableUseCase
import javax.inject.Inject
import kotlin.fold

interface GetTopHeadlinesUseCase : SuspendableUseCase<NewsResponse, Map<String, Any>>

class GetTopHeadlinesUseCaseImpl @Inject constructor(val repository: NewsRepository) :
    GetTopHeadlinesUseCase {

    override suspend fun invoke(params: Map<String, Any>): Result<NewsResponse> {
        return try {
            val country = params["country"] as String
            val category = params["category"] as String?
            val newsResponseDtoResult: Result<NewsResponseDto> = repository.getTopHeadlines(country, category)

            newsResponseDtoResult.fold(
                onSuccess = { newsResponseDto ->
                    // Optionally, get favorites URLs from repository
                    val favoritesUrls = repository.getFavoritesUrls()
                    println("Favorites URLs: $favoritesUrls")

                    // Transform DTO -> Domain
                    val newsResponse = newsResponseDto.toDomain(favoritesUrls)
                    println(newsResponse)
                    Result.success(newsResponse)
                },
                onFailure = { e ->
                    Result.failure(e)
                }
            )

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}