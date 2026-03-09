package be.business.newsapp.core.di

import be.business.newsapp.core.data.local.datastore.DataManager
import be.business.newsapp.core.data.remote.apis.ApiService
import be.business.newsapp.core.data.repository.NewsRepositoryImpl
import be.business.newsapp.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideNewsRepository(apiService: ApiService,dataManager: DataManager): NewsRepository =
        NewsRepositoryImpl(apiService,dataManager)

}