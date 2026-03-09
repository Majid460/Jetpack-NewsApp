package be.business.androidpractice.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ElevatedBtn(text: String,onClick: () -> Unit) {
    ElevatedButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color.Blue,   // Background color
            contentColor = Color.White,           // Text (and icon) color
            disabledContainerColor = Color.Gray,  // Background when disabled
            disabledContentColor = Color.LightGray
        )
    ) {
        Text(text = text)
    }
}