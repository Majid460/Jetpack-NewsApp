package be.business.newsapp.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import be.business.newsapp.core.data.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferenceRepository {

    override suspend fun isDarkTheme(value: Boolean) {
        dataStore.edit { prefs -> prefs[Keys.THEME_MODE] = value }
    }

    override suspend fun isDarkTheme(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[Keys.THEME_MODE] ?: false
        }
    }

    private object Keys {
        val THEME_MODE = booleanPreferencesKey("theme_mode")
        val ONBOARDING_SEEN = booleanPreferencesKey("onboarding_seen")
        val SEARCH_QUERIES = stringSetPreferencesKey("search_queries")
    }


    override val onboardingSeen: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.ONBOARDING_SEEN] ?: false
    }

    override suspend fun setOnboardingSeen(seen: Boolean) {
        dataStore.edit { prefs -> prefs[Keys.ONBOARDING_SEEN] = seen }
    }

    override val searchQueries: Flow<Set<String>> = dataStore.data.map { prefs ->
        prefs[Keys.SEARCH_QUERIES] ?: emptySet()
    }

    override suspend fun addSearchQuery(query: String) {
        dataStore.edit { prefs ->
            val current = prefs[Keys.SEARCH_QUERIES] ?: emptySet()
            prefs[Keys.SEARCH_QUERIES] = current + query
        }
    }

    override suspend fun clearSearchHistory() {
        dataStore.edit { prefs -> prefs[Keys.SEARCH_QUERIES] = emptySet() }
    }
}
