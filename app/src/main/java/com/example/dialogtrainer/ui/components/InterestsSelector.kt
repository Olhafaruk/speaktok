//ui/components/InterestsSelector.kt
package com.example.dialogtrainer.ui.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dialogtrainer.data.model.Interest
import com.example.dialogtrainer.data.model.allInterests

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestsSelector(
    selectedInterests: Set<Interest>,
    onSelectionChange: (Set<Interest>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Interests",
        style = MaterialTheme.typography.titleMedium,
    )

    Spacer(modifier = Modifier.height(8.dp))

    FlowRow(
        modifier = modifier,
        maxItemsInEachRow = 3,
    ) {
        allInterests.forEach { interest ->
            val isSelected = interest in selectedInterests

            FilterChip(
                selected = isSelected,
                onClick = {
                    val newSelection = if (isSelected) {
                        selectedInterests - interest
                    } else {
                        selectedInterests + interest
                    }
                    onSelectionChange(newSelection)
                },
                label = { Text(interest.title) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        }
    }
}
