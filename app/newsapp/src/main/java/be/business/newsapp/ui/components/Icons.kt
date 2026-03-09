package be.business.newsapp.ui.components

import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.business.newsapp.MainAction
import be.business.newsapp.ui.theme.AndroidPracticeTheme


data class IconPair(
    val filled: ImageVector,
    val outlined: ImageVector
)

object CustomIcons {

    // Home Icons
    val Home = IconPair(
        filled = Icons.Filled.Home,
        outlined = Icons.Outlined.Home
    )

    // Search Icons
    val Search = IconPair(
        filled = Icons.Filled.Search,
        outlined = Icons.Outlined.Search
    )

    // Favorite Icons
    val Favorite = IconPair(
        filled = Icons.Filled.Favorite,
        outlined = Icons.Outlined.FavoriteBorder
    )

    // Profile/Person Icons
    val Profile = IconPair(
        filled = Icons.Filled.Person,
        outlined = Icons.Outlined.Person
    )

    // Settings Icons
    val Settings = IconPair(
        filled = Icons.Filled.Settings,
        outlined = Icons.Outlined.Settings
    )

    // Bookmark Icons
    val Bookmark = IconPair(
        filled = Icons.Filled.Bookmark,
        outlined = Icons.Outlined.BookmarkBorder
    )

    // News/Article Icons
    val Article = IconPair(
        filled = Icons.AutoMirrored.Default.Article,
        outlined = Icons.AutoMirrored.Outlined.Article
    )

    // Notifications Icons
    val Notifications = IconPair(
        filled = Icons.Filled.Notifications,
        outlined = Icons.Outlined.Notifications
    )

    // Category Icons
    val Category = IconPair(
        filled = Icons.Filled.Category,
        outlined = Icons.Outlined.Category
    )

    // Explore Icons
    val Explore = IconPair(
        filled = Icons.Filled.Explore,
        outlined = Icons.Outlined.Explore
    )

    // Trending Icons
    val TrendingUp = IconPair(
        filled = Icons.AutoMirrored.Filled.TrendingUp,
        outlined = Icons.AutoMirrored.Outlined.TrendingUp
    )

    // Menu Icons
    val Menu = IconPair(
        filled = Icons.Filled.Menu,
        outlined = Icons.Outlined.Menu
    )

    // Dashboard Icons
    val Dashboard = IconPair(
        filled = Icons.Filled.Dashboard,
        outlined = Icons.Outlined.Dashboard
    )

    // Video Icons
    val VideoLibrary = IconPair(
        filled = Icons.Filled.VideoLibrary,
        outlined = Icons.Outlined.VideoLibrary
    )

    // Account Icons
    val AccountCircle = IconPair(
        filled = Icons.Filled.AccountCircle,
        outlined = Icons.Outlined.AccountCircle
    )
}

@Composable
fun FilterIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = rememberVectorPainter(Icons.Default.FilterList),
            contentDescription = "Filter",
            tint = tint
        )
    }
}


@Composable
fun ThemeIcon(isDarkMode: Boolean = false,onClick: () -> Unit,) {
    IconButton(onClick = onClick, modifier = Modifier.padding(end = 10.dp)
        .background(color = MaterialTheme.colorScheme.tertiaryContainer,shape = CircleShape)) {
        Icon(
            painter = rememberVectorPainter(if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode),
            contentDescription = "Theme",
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview
@Composable
fun PreviewThemeIcon() {
    AndroidPracticeTheme() {
        ThemeIcon(onClick = {})
    }
}