package be.business.newsapp.feature.favorite.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import be.business.newsapp.feature.favorite.FavouritesScreen
import be.business.newsapp.navigation.NewsScreen

fun EntryProviderScope<NavKey>.featureFavourites() {

    entry< NewsScreen.Favourites> {
        FavouritesScreen()
    }
}