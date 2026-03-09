package be.business.newsapp.core.di

import be.business.newsapp.core.data.repository.NewsRepositoryImpl
import be.business.newsapp.domain.repository.NewsRepository
import be.business.newsapp.domain.usecase.news.AddToFavouritesUseCase
import be.business.newsapp.domain.usecase.news.AddToFavouritesUseCaseImpl
import be.business.newsapp.domain.usecase.news.GetFavouriteNewsUseCase
import be.business.newsapp.domain.usecase.news.GetFavouriteNewsUseCaseImpl
import be.business.newsapp.domain.usecase.news.GetTopHeadlinesUseCase
import be.business.newsapp.domain.usecase.news.GetTopHeadlinesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetTopNewsUseCase(repo: NewsRepository): GetTopHeadlinesUseCase = GetTopHeadlinesUseCaseImpl(repo)

    @Provides
    @Singleton
    fun providesAddToFavouritesUseCase(repo: NewsRepository): AddToFavouritesUseCase =
        AddToFavouritesUseCaseImpl(repo)

    @Provides
    @Singleton
    fun provideGetFavouriteNewsUseCase(repo: NewsRepository): GetFavouriteNewsUseCase =
        GetFavouriteNewsUseCaseImpl(repo)


}