package se.curtrune.lucy.classes.mental_fragment

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import se.curtrune.lucy.classes.Item

@Composable
fun EditItemsScreen(
    state: MentalState,
    onEvent: (MentalEvent)->Unit
) {
    Button(onClick = {
        onEvent(MentalEvent.UpdateItem(Item()))
    }){

    }


}