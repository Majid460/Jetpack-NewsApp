package be.business.newsapp.ui.components

import android.R.attr.scaleX
import android.R.attr.scaleY
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun AnimatedFavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(targetState = isFavorite, label = "fav_transition")

    // ⭐ Scale animation
    val scale by transition.animateFloat(
        label = "scale_anim",
        transitionSpec = {
            if (targetState) {
                // ❤️ Add to favorite → POP OUT
                spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            } else {
                // 💔 Remove → Shrink then return
                tween(durationMillis = 200)
            }
        }
    ) { fav ->
        if (fav) 1.4f else 1f   // enlarges slightly when favorited
    }

    // ⭐ Rotation animation
    val rotation by transition.animateFloat(
        label = "rotation_anim",
        transitionSpec = {
            tween(durationMillis = 300)
        }
    ) { fav ->
        if (fav) 360f else 0f   // rotate on add
    }

    // ⭐ Alpha fade animation on unfavorite
    val alpha by transition.animateFloat(
        label = "alpha_anim",
        transitionSpec = {
            tween(durationMillis = 200)
        }
    ) { fav ->
        if (fav) 1f else 0.6f
    }

    IconButton(
        onClick = onClick,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationZ = rotation
                this.alpha = alpha
            }
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite",
                tint = if (isFavorite) Color.Red else Color.Gray
        )
    }
}
