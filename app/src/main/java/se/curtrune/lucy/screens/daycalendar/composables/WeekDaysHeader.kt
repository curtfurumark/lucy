package se.curtrune.lucy.screens.daycalendar.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.daycalendar.DayEvent
import se.curtrune.lucy.screens.daycalendar.DayCalendarState
import se.curtrune.lucy.util.cecilia
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DayOfWeek(date: LocalDate, state: DayCalendarState, onEvent: (DayEvent) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onEvent(DayEvent.CurrentDate(date)) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //isSystemInDarkTheme()
        val color = if (date == state.date) {
            if( isSystemInDarkTheme()){
                Color.White
            }else{
                Color.Black
            }
        } else Color.Gray

        Text(
            text =
            date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                .cecilia(),
            color = color
        )
        Text(text = date.dayOfMonth.toString(), color = color)
    }
}

@Composable
fun DaysOfWeek(state: DayCalendarState, onEvent: (DayEvent)->Unit){
    Column(modifier = Modifier.fillMaxWidth()) {
/*        val header = "${state.date.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()).cecilia()} v ${state.currentWeek.weekNumber}"
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
            Text(text = header)
        }*/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            var date = state.currentWeek.firstDateOfWeek
            for (i in 1..7) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .border(BorderStroke(Dp.Hairline, color = Color.DarkGray)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DayOfWeek(date, state, onEvent = onEvent)
                    date = date.plusDays(1)
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
@Preview(showBackground = true)
fun PreviewWeek(){
    LucyTheme {
        DaysOfWeek(state = DayCalendarState(), onEvent = {})
    }
}
