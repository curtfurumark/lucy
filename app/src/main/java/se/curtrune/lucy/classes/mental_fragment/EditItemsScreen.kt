package se.curtrune.lucy.classes.mental_fragment

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.my_day.MyDateEvent
import se.curtrune.lucy.screens.my_day.MyDayState

@Composable
fun EditItemsScreen(
    state: MyDayState,
    onEvent: (MyDateEvent)->Unit
) {
    Button(onClick = {
        onEvent(MyDateEvent.UpdateItem(Item()))
    }){

    }


}