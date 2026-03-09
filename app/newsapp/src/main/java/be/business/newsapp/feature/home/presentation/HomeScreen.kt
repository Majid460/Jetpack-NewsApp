package be.business.newsapp.feature.home.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import be.business.newsapp.core.presentation.collectEvents
import be.business.newsapp.domain.model.Article
import be.business.newsapp.feature.home.components.TopNewsSection
import be.business.newsapp.ui.components.ErrorView
import be.business.newsapp.ui.components.LoadingView
import coil.ImageLoader

@Composable
fun HomeScreen(
    onArticleClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()

    val context = LocalContext.current

    viewModel.collectEvents {
        when (it) {
            is HomeEvent.ShowToast -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    HomeContent(
        uiState = uiState,
        imageLoader = viewModel.imageLoader,
        chipSelected = viewModel.selectedCategory,
        selectedCountry = viewModel.selectedCountry,
        onRetry = {
            viewModel.action(HomeAction.FetchTopNewsData(viewModel.selectedCountry))
        },
        onFavClick = {
            viewModel.action(HomeAction.AddToFavourites(it))
        },
        onChipClick = {
            viewModel.selectCategory(it)
        },
        onCountrySelected = {
            viewModel.selectCountry(it)
        }
    )
}

@Composable
fun HomeContent(
    uiState: HomeState,
    imageLoader: ImageLoader,
    chipSelected: String? = null,
    selectedCountry: String? = null,
    onRetry: () -> Unit,
    onFavClick: (Article) -> Unit,
    onChipClick: (String) -> Unit,
    onCountrySelected: (String) -> Unit
) {
    when {
        uiState.error != null -> {
            ErrorView(uiState.error) { onRetry() }
        }
        uiState.topNews != null -> {
            TopNewsSection(
                newsResponse = uiState.topNews,
                imageLoader = imageLoader,
                onFavClick = onFavClick,
                chipSelected = chipSelected,
                selectedCategory = onChipClick,
                isLoading = uiState.loading,
                showChips = true,
                countrySelected = selectedCountry,
                onCountrySelected = onCountrySelected,
                showFilterView = true
            )
        }
        else -> {
            LoadingView()
        }
    }
}

@Composable
fun EmptyView() {
    // Empty view
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            "No data available",
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}



