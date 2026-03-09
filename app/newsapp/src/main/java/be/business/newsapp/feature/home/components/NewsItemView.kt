package be.business.newsapp.feature.home.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import be.business.newsapp.domain.model.Article
import be.business.newsapp.ui.ScreenSizeConfig
import be.business.newsapp.ui.components.AnimatedFavoriteButton
import be.business.newsapp.ui.components.NewsImage
import be.business.newsapp.utils.marginVertical
import coil.ImageLoader

@Composable
fun NewsItemView(
    article: Article,
    imageLoader: ImageLoader,
    onFavClick: (article: Article) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                onClick = {

                },
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() })
    ) {
        NewsImage(
            article.urlToImage ?: "", imageLoader, modifier = Modifier
                .size(
                    ScreenSizeConfig.rememberWindowSize().width,
                    height = ScreenSizeConfig.getDimensions().imageHeight
                )
                .weight(3f)
        )
        5.marginVertical()
        Row(Modifier.weight(1f)) {
            Text(
                article.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(3.5f),
                style = MaterialTheme.typography.titleMedium
            )

            AnimatedFavoriteButton(
                article.isFavorite,
                onClick = { onFavClick(article) },
                modifier = Modifier
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
            )
        }


    }
}