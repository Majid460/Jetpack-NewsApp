package be.business.newsapp.core.data.remote.apiimpl

import be.business.newsapp.core.data.remote.apis.ApiService
import be.business.newsapp.core.data.remote.apis.requests.NewsResource
import be.business.newsapp.domain.model.newsresponse.NewsResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import javax.inject.Inject

class ApiImpl @Inject constructor(val client: HttpClient) : ApiService {

    override suspend fun getTopHeadlinesOfCountry(country: String, category: String?): Result<NewsResponseDto>{
       return try {
            val resource = NewsResource(country, category = category)
            val response = client.get(resource).body<NewsResponseDto>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTopHeadlinesOfChannel(sources: String): Result<NewsResponseDto> {
        return try {
            val resource = NewsResource(sources = sources)
            val response = client.get(resource).body<NewsResponseDto>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}