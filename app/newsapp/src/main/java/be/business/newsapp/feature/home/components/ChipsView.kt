package be.business.newsapp.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


// Chips view
@Composable
fun ChipsView(
    filters: List<FilterItem>,
    selectedLabel: String?,
    onSelectionChange: (String?) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        filters.forEach { filter ->
            val isSelected = filter.label.equals(selectedLabel, ignoreCase = true)
            println("selectedLabel = $selectedLabel, filter = ${filter.label}, isSelected = $isSelected")

            ElevatedFilterChip(
                selected = isSelected,
                onClick = {
                    val newSelection = if (isSelected) null else filter.label
                    onSelectionChange(newSelection)
                },
                label = { Text(filter.label) },
                trailingIcon = {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    }
}
