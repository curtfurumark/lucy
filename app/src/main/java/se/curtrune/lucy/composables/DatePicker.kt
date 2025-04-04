package se.curtrune.lucy.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.screens.my_day.MyDayState
import java.time.LocalDate

@Composable
fun MyDatePicker(state: MyDayState, onDate: (LocalDate)->Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = state.date.toString(),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp)
    }
}
@Composable
fun AllDaySwitch(allDay: Boolean,onEstimate: (Boolean)->Unit){
    var isAllDay by remember{
     mutableStateOf( allDay)
    }
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "all day", color = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Switch(checked = isAllDay, onCheckedChange = {
            isAllDay = !isAllDay
            onEstimate(it)
        })
    }
}