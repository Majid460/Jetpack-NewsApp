package be.business.newsapp.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import be.business.newsapp.core.data.local.datastore.PreferenceRepositoryImpl
import be.business.newsapp.core.data.repository.PreferenceRepository
import be.business.newsapp.utils.dataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataStoreModule {
    @Binds
    @Singleton
    abstract fun providePreferenceRepository(
        preferencesRepositoryImpl: PreferenceRepositoryImpl
    ): PreferenceRepository

    companion object {
        @Provides
        @Singleton
        fun providePreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.dataStore
        }
    }
}