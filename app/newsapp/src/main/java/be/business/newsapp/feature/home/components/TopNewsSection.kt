package be.business.newsapp.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import be.business.newsapp.domain.model.Article
import be.business.newsapp.domain.model.NewsResponse
import be.business.newsapp.feature.home.presentation.EmptyView
import be.business.newsapp.ui.components.FilterIconButton
import be.business.newsapp.ui.components.LoadingView
import be.business.newsapp.ui.theme.AndroidPracticeTheme
import be.business.newsapp.utils.countries
import be.business.newsapp.utils.marginVertical
import coil.ImageLoader

data class FilterItem(
    val label: String
)

val filters = listOf(
    FilterItem("Business"),
    FilterItem("Sports"),
    FilterItem("Tech"),
    FilterItem("Health")
)

@Composable
fun TopNewsSection(
    newsResponse: NewsResponse? = null,
    isLoading: Boolean = false,
    chipSelected: String? = null,
    selectedCategory: (String) -> Unit,
    articles: List<Article> = newsResponse?.article ?: emptyList(),
    imageLoader: ImageLoader,
    onFavClick: ((article: Article) -> Unit)? = null,
    showChips: Boolean = false,
    showFilterView: Boolean = false,
    countrySelected: String? = null,
    onCountrySelected: (String) -> Unit = {}

) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Column() {
            if (showFilterView)
                CountrySelectionDropDown(
                    selectedCountry = countrySelected,
                    onCountrySelected = {
                        onCountrySelected.invoke(it.lowercase())
                    }
                )
            if (showChips) ChipsView(
                filters = filters,
                selectedLabel = chipSelected,
                onSelectionChange = { filter ->
                    selectedCategory.invoke(filter?.lowercase() ?: "")
                }
            )
            5.marginVertical()
            if (articles.isEmpty()) {
                EmptyView()
            } else if (isLoading) {
                LoadingView()
            } else LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(articles) { article ->
                    NewsItemView(article, imageLoader, onFavClick = { onFavClick?.invoke(article) })
                    5.marginVertical()
                }
            }
        }

    }
}

@Composable
fun CountryView(selectedCountry: String? = null, modifier: Modifier = Modifier) {
    val countryName = countries.find { it.countryCode == selectedCountry }?.countryName
    Text(
        "Country: $countryName",
        modifier = modifier.padding(vertical = 10.dp),
        style = MaterialTheme.typography.labelMedium,
        textAlign = TextAlign.Center
    )
}

@Composable
fun CountrySelectionDropDown(
    selectedCountry: String? = null,
    onCountrySelected: (String) -> Unit = {}
) {
    val countriesList = countries.map { "${it.countryName} - ${it.countryCode}" }
    val isExpanded = remember { mutableStateOf(false) }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        CountryView(selectedCountry)
        Box {
            FilterIconButton(onClick = { isExpanded.value = true })
            DropdownMenu(
                expanded = isExpanded.value,
                onDismissRequest = { isExpanded.value = false }
            ) {
                countriesList.forEach { option ->
                    val code = option.split(" - ")[1]

                    DropdownMenuItem(
                        leadingIcon = {
                            if (selectedCountry.equals(code, ignoreCase = true)) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        text = { Text(option) },
                        onClick = {
                            onCountrySelected(code)
                            isExpanded.value = false
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TopNewsSectionPreview() {
    AndroidPracticeTheme() {
        TopNewsSection(
            newsResponse = NewsResponse(status = "", totalResults = 0, article = emptyList()),
            isLoading = false,
            chipSelected = null,
            selectedCategory = {},
            articles = emptyList(),
            imageLoader = ImageLoader(LocalContext.current),
            onFavClick = null,
            showChips = true,
            countrySelected = "us",
            onCountrySelected = {}
        )

    }
}