package be.business.newsapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// Home Routes

sealed interface NewsScreen : NavKey {
    @Serializable
    object Home : NewsScreen

    @Serializable
    data class ArticleDetails(val articleId: String) : NewsScreen

    @Serializable
    object Profile : NewsScreen

    @Serializable
    object Favourites : NewsScreen
}

data class NavBarItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val description: String
)

val TOP_LEVEL_ROUTES = mapOf<NavKey, NavBarItem>(
    NewsScreen.Home to NavBarItem(
        selectedIcon = Icons.Filled.Newspaper,
        unselectedIcon = Icons.Outlined.Newspaper,
        description = "Top Headlines"
    ),

    NewsScreen.Favourites to NavBarItem(
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
        description = "Favorites"
    ),

    NewsScreen.Profile to NavBarItem(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        description = "Profile"
    )
)