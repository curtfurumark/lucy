package se.curtrune.lucy.screens.timeline.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SortBar(onEvent: (SortEvent)->Unit){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier.clickable(
                onClick = {
                    onEvent(SortEvent.SortAlphabetically)
                }
            )
                .padding(8.dp),
            imageVector = Icons.Default.SortByAlpha,
            contentDescription = "sort by alpha"
        )
        Text(text = "date desc", modifier = Modifier.clickable(
            onClick = {
                onEvent(SortEvent.SortDateDescending)
            }
        ))
        Text(text = "date asc", modifier = Modifier.clickable(
            onClick = {
                onEvent(SortEvent.SortDateAscending)
            }
        ))
        Text(text = "priority", modifier = Modifier.clickable(
            onClick = {
                onEvent(SortEvent.SortPriority)
            }
        ))
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewSortBar(){


}