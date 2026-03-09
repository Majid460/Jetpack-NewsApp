package be.business.newsapp.navigation

import androidx.navigation3.runtime.entryProvider
import be.business.newsapp.feature.favorite.navigation.featureFavourites
import be.business.newsapp.feature.home.navigation.featureHome
import be.business.newsapp.feature.profile.navigation.featureProfile

fun entryProvider(navigator: Navigator) = entryProvider {
    featureHome(onArticleClick = { navigator.navigate(NewsScreen.ArticleDetails(it))})
    featureFavourites()
    featureProfile()
}