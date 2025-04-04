package se.curtrune.lucy.screens.monthcalendar.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekDaysHeader() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        DayOfWeek.entries.iterator().forEach {
            Column(
                modifier = Modifier.weight(1f)
                    .border(BorderStroke(Dp.Hairline, color = Color.Gray)),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = it.getDisplayName(TextStyle.NARROW, Locale.getDefault()).uppercase(),
                )
            }
        }
    }
}
@Preview
@Composable
fun Preview(){
    WeekDaysHeader()
}