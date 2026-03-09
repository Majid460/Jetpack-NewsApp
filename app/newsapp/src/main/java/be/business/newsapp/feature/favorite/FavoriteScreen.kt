package be.business.newsapp.feature.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import be.business.newsapp.core.presentation.collectEvents
import be.business.newsapp.feature.home.components.TopNewsSection
import be.business.newsapp.ui.components.ErrorView
import be.business.newsapp.ui.components.LoadingView
import be.business.newsapp.ui.theme.AndroidPracticeTheme
import coil.ImageLoader

@Composable
fun FavouritesScreen(viewModel: FavoriteViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState()
    val uiState = state.value
    FavoriteContent(uiState = uiState, imageLoader = viewModel.imageLoader, onRetry = {
        viewModel.action(FavoriteAction.FetchFavNewsData)
    })

    viewModel.collectEvents {
        when (it) {
            is FavoriteEvent.ShowToast -> {
                // Show toast
            }

            else -> Unit
        }
    }
}

@Composable
fun FavoriteContent(uiState: FavoriteState, imageLoader: ImageLoader, onRetry: () -> Unit) {
    when (uiState) {
        is FavoriteState.Loading -> {
            LoadingView()
        }

        is FavoriteState.Success -> {
            if (uiState.data.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "No favourites available", textAlign =  TextAlign.Center, modifier = Modifier.align(Alignment.Center))
                }

            }
            TopNewsSection(
                articles = uiState.data,
                imageLoader = imageLoader,
                selectedCategory = {}
            )
        }

        is FavoriteState.Error -> {
            ErrorView(uiState.message) {
                onRetry()

            }
        }

        else -> {}
    }

}

@Composable
@Preview(showBackground = true)
fun favouritesScreenPreview() {
    AndroidPracticeTheme {
        FavoriteContent(
            uiState = FavoriteState.Initial,
            imageLoader = ImageLoader(LocalContext.current),
            {}
        )
    }
}