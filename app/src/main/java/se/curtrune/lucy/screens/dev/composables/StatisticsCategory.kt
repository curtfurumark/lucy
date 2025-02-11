package se.curtrune.lucy.screens.dev.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.screens.util.Converter
import se.curtrune.lucy.statistics.Statistics

@Composable
fun StatisticsCategory(modifier: Modifier = Modifier, heading: String, items: List<Item>) {
    Text(text = heading.ifEmpty { "no category" }, fontSize = 20.sp)
    Text(text = "duration: ${Converter.formatSecondsWithHours(items.sumOf { item -> item.duration })}")
}
