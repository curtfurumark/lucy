package se.curtrune.lucy.screens.my_day.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.MentalMeter4
import se.curtrune.lucy.screens.my_day.MyDateEvent
import se.curtrune.lucy.screens.my_day.MyDayState

@Composable
fun MyDayItemList(state: MyDayState, onEvent: (MyDateEvent)->Unit){
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(state.items) { item: Item ->
            println("item ${item.heading}")
            //Text(text = item.heading, color = Color.White, fontSize = 24.sp)
            MentalMeter4(item, state = state, onEvent = onEvent)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }

}