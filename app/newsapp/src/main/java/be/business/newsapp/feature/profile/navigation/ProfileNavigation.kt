package be.business.newsapp.feature.profile.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import be.business.newsapp.feature.profile.ProfileScreen
import be.business.newsapp.navigation.NewsScreen

fun EntryProviderScope<NavKey>.featureProfile() {

    entry<NewsScreen.Profile> {
       ProfileScreen()
    }
}