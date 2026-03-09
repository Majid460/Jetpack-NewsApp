package be.business.newsapp.core.di

import android.content.Context
import androidx.room.Room
import be.business.newsapp.R
import be.business.newsapp.core.data.local.datastore.DataManager
import be.business.newsapp.core.data.local.datastore.PreferenceRepositoryImpl
import be.business.newsapp.core.data.local.room.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDataManager(
        preferencesRepositoryImpl: PreferenceRepositoryImpl,
        database: NewsDatabase
    ): DataManager = DataManager(preferencesRepositoryImpl, database)
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
                context,
                NewsDatabase::class.java,
                context.getString(R.string.app_name)
            ).fallbackToDestructiveMigration(false).build()
    }
}