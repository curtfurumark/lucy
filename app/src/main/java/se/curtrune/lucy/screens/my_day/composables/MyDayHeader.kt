package se.curtrune.lucy.screens.my_day.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.composables.ItemFieldChooser
import se.curtrune.lucy.composables.MyDatePicker
import se.curtrune.lucy.screens.my_day.MyDayEvent
import se.curtrune.lucy.screens.my_day.MyDayState

@Composable
fun MyDayHeader(state: MyDayState, onEvent: (MyDayEvent)->Unit){
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MyDatePicker(state = state, onDate = { date ->
            println(" on date chosen")
            onEvent(MyDayEvent.Date(date))
        })
        Spacer(modifier = Modifier.height(8.dp))
        SelectAllDayOrCurrent(state.allDay, onEvent = { event ->
            println("on all day event")
            onEvent(event)
        })
        ItemFieldChooser(onFieldChosen = { field ->
            println("field chosen, ${field.name}")
            onEvent(MyDayEvent.Field(field))
        })
    }
}