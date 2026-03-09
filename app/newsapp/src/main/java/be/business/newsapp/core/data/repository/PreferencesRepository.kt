package be.business.newsapp.core.data.repository

import kotlinx.coroutines.flow.Flow


interface PreferenceRepository {

    // Theme
    suspend fun isDarkTheme(value: Boolean)

    suspend fun isDarkTheme(): Flow<Boolean>

    // Onboarding
    val onboardingSeen: Flow<Boolean>
    suspend fun setOnboardingSeen(seen: Boolean)

    // Search history
    val searchQueries: Flow<Set<String>>
    suspend fun addSearchQuery(query: String)
    suspend fun clearSearchHistory()
}
