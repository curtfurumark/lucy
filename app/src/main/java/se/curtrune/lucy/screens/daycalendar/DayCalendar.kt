package se.curtrune.lucy.screens.daycalendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.calender.Week
import java.time.LocalDate


@Composable
fun DayCalendar(state: DayCalendarState, onEvent: (DateEvent)->Unit){
    Column() {
        Header()
        DaysOfWeek(Week(), onEvent ={
            println("on date click")
            onEvent(it)
        } )
        LazyColumn {
            items(state.items.size) { index ->
                DateItem(state.items[index], onEvent = {
                    onEvent(it)
                })
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}





@Composable
fun Header(){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        Text(text = LocalDate.now().toString())
    }
}