package se.curtrune.lucy.screens.my_day.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import se.curtrune.lucy.composables.Field
import se.curtrune.lucy.screens.my_day.MyDateEvent
import se.curtrune.lucy.screens.my_day.MyDayState

@Composable
fun MentalScreen(state: MyDayState, onEvent: (MyDateEvent)->Unit){
    var currentField by remember{
        mutableStateOf(Field.ENERGY)
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        MyDayHeader(state = state, onEvent = onEvent)
        MyDayItemList(state = state, onEvent = onEvent)
    }
/*    Column(modifier = Modifier.fillMaxWidth()
        .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally){
        //MentalMeter(mental = Mental())
        MyDatePicker(state = state, onDate = { date ->
            println(" on date chosen")
            //mentalDateViewModel.setDate(date)
        })
        Spacer(modifier = Modifier.height(8.dp))
        SelectAllDayOrCurrent(state.allDay, onEvent = { event->
            println("on all day event")
            onEvent(event)
        })
        ItemFieldChooser(onFieldChosen = {
            println("field chosen, ${it.name}")
            currentField = it
        })
        state.items.forEach{ item->
            MentalMeter4(item = item, field =  currentField, onLevelChange = { level->
                println("onLevelChange $level")
            })
            Spacer(modifier = Modifier.height(4.dp))
        }
    }*/
}
