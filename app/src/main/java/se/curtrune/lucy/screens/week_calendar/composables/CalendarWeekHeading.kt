package se.curtrune.lucy.screens.week_calendar.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.util.cecilia
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarWeekHeading(week: Week) {
    Row( modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = formatWeek(week),
            fontSize = 18.sp,
            textAlign = TextAlign.Center)
    }
}
fun formatWeek(week: Week):String{
    return String.format(Locale.getDefault(), "%s v%d", week.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).cecilia(), week.weekNumber)
}

@Preview(showBackground = true)
@Composable
fun PreviewWeekHeading(){
    LucyTheme {
        CalendarWeekHeading(week = Week())
    }

}