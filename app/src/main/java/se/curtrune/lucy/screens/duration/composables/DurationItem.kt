package se.curtrune.lucy.screens.duration.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.screens.util.Converter
import se.curtrune.lucy.statistics.CategoryListable

@Composable
fun DurationItem(item: CategoryListable) {
    Column(){
        Card(modifier = Modifier.fillMaxWidth().background(Color.Gray)) {
            Text(text = item.heading, fontSize = 20.sp, color = Color.Black)
            Text(
                text = "duration ${Converter.formatSecondsWithHours(item.duration)}",
                color = Color.Black
            )
        }
    }
}