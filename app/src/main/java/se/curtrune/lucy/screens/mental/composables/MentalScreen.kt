package se.curtrune.lucy.screens.mental.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.kotlin.composables.EditItemCard
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.composables.Field
import se.curtrune.lucy.composables.ItemFieldChooser
import se.curtrune.lucy.composables.MentalMeter
import se.curtrune.lucy.composables.MentalMeter4
import se.curtrune.lucy.composables.MyDatePicker
import se.curtrune.lucy.screens.mental.MentalEvent
import se.curtrune.lucy.screens.mental.MentalState

@Composable
fun MentalScreen(state: MentalState, onEvent: (MentalEvent)->Unit){
    var currentField by remember{
        mutableStateOf(Field.ENERGY)
    }
    Column(modifier = Modifier.fillMaxWidth(),
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
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            println("lazy column")
            items(state.items){ item->
                MentalMeter4(item = item, field =  currentField, onLevelChange = { level->
                    println("onLevelChange $level")
                })
                Spacer(modifier = Modifier.height(4.dp))
/*                EditItemCard(item, onItemEdit = {
                    println("item edited, ${it.heading}")
                    onEvent(MentalEvent.UpdateItem(it))
                }, itemField = currentField)*/
            }
        }
    }
}
