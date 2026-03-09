package be.business.newsapp.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.ui.NavDisplay
import be.business.newsapp.MainAction
import be.business.newsapp.MainViewModel
import be.business.newsapp.core.presentation.AppState
import be.business.newsapp.ui.components.BottomNavItem
import be.business.newsapp.ui.components.ThemeIcon
import be.business.newsapp.ui.rememberResponsiveDimensions
import be.business.newsapp.ui.theme.AndroidPracticeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsAppNavDisplay(viewModel: MainViewModel = hiltViewModel()) {
    val theme: Boolean = AppState.isDarkTheme.collectAsState().value
    // Initial screen: Home
    val navigationState = rememberNavigationState(
        startRoute = NewsScreen.Home,
        topLevelRoutes = setOf(NewsScreen.Home, NewsScreen.Favourites, NewsScreen.Profile)
    )

    val navigator = remember { Navigator(navigationState) }

    // Get responsive dimensions from singleton
    val dimensions = rememberResponsiveDimensions()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    AndroidPracticeTheme {
        Surface {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                val item = TOP_LEVEL_ROUTES[navigator.state.topLevelRoute]
                                Text(
                                    item?.description ?: "",
                                    maxLines = 1,
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        },
                        actions = {
                            ThemeIcon(isDarkMode = theme){
                               viewModel.action(MainAction.ChangeTheme(!theme))
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
                bottomBar = {
                    // Wrapper to center the bottom bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .padding(bottom = dimensions.bottomBarBottomPadding),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            modifier = Modifier
                                .widthIn(max = dimensions.bottomBarMaxWidth)
                                .fillMaxWidth()
                                .padding(horizontal = dimensions.bottomBarHorizontalPadding)
                                .height(dimensions.bottomBarHeight)
                                .clip(RoundedCornerShape(dimensions.bottomBarCornerRadius)),
                            tonalElevation = dimensions.cardElevation,
                            shadowElevation = dimensions.cardElevation,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 4.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TOP_LEVEL_ROUTES.forEach { (key, value) ->
                                    BottomNavItem(
                                        icon = value.selectedIcon,
                                        unselectedIcon = value.unselectedIcon,
                                        label = value.description,
                                        selected = key == navigationState.topLevelRoute,
                                        onClick = { navigator.navigate(key) }
                                    )
                                }
                            }
                        }
                    }
                }
            ) { paddingValues ->
                NavDisplay(
                    entries = navigationState.toEntries(entryProvider(navigator)),
                    transitionSpec = {
                        // Slide in from right when navigating forward
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { -it })
                    },
                    popTransitionSpec = {
                        // Slide in from left when navigating back
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { it })
                    },
                    onBack = { navigator.goBack() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewNews() {
    AndroidPracticeTheme {
        NewsAppNavDisplay()
    }

}