package be.business.newsapp.feature.home.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import be.business.newsapp.feature.articledetail.ArticleDetailsScreen
import be.business.newsapp.feature.home.presentation.HomeScreen
import be.business.newsapp.navigation.NewsScreen

fun EntryProviderScope<NavKey>.featureHome(onArticleClick: (String) -> Unit) {
    entry<NewsScreen.Home> {
        HomeScreen(
            onArticleClick = { id ->
               onArticleClick(id)
            }
        )
    }

    entry<NewsScreen.ArticleDetails> { _ ->
        ArticleDetailsScreen(
            onArticleClick = { id ->
                onArticleClick(id)
            }
        )
    }
}
