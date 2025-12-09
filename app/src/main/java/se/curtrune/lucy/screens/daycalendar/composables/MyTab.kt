package se.curtrune.lucy.screens.daycalendar.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.screens.daycalendar.DayCalendarEvent

@Composable
fun MyTab(heading: String, index: Int, onEvent: (DayCalendarEvent)->Unit){
    Box(
        modifier = Modifier.border(width = Dp.Hairline, color = Color.Yellow)
            .clickable {
                onEvent(DayCalendarEvent.TabSelected(index = index, item = null))
            }
            .padding(4.dp)){
        Text(text = heading)
    }
}