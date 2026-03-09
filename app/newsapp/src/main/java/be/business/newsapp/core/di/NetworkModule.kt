package be.business.newsapp.core.di

import be.business.newsapp.BuildConfig
import be.business.newsapp.core.data.remote.apiimpl.ApiImpl
import be.business.newsapp.core.data.remote.apis.ApiService
import be.business.newsapp.core.data.remote.network.NetworkConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import jakarta.inject.Named
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("BASE_URL")
    fun provideBaseUrl(): String = NetworkConfig.BASE_URL

    @Provides
    @Singleton
    @Named("API_KEY")
    fun provideApiKey(): String = BuildConfig.NEWS_API_KEY

    @Provides
    @Singleton
    fun provideHttpClient(
        @Named("BASE_URL") baseUrl: String,
        @Named("API_KEY") apiKey: String
    ): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Resources)
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "newsapi.org"
                    parameters.append("apikey", apiKey)
                }
            }
            engine {
                requestTimeout = 10000
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 10000
            }
        }
    }

    @Provides
    @Singleton
    fun provideApiImpl(client: HttpClient): ApiService = ApiImpl(client)
}
