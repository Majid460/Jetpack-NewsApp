package be.business.newsapp.utils

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Int.marginVertical() = Spacer(Modifier.height(this.dp))

@Composable
fun Int.marginHorizontal() = Spacer(Modifier.width(this.dp))