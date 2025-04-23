package se.curtrune.lucy.screens.my_day.composables

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.MentalMeter4
import se.curtrune.lucy.screens.my_day.MyDayEvent
import se.curtrune.lucy.screens.my_day.MyDayState

@Composable
fun MyDayItemList(state: MyDayState, onEvent: (MyDayEvent)->Unit){
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(state.items) { item: Item ->
            //println("item ${item.heading}")
            //Text(text = item.heading, color = Color.White, fontSize = 24.sp)
            //MentalItem(item, state = state, onEvent = onEvent)
            //MentalMeter4(item = item, state = state, onEvent = onEvent)
            MentalMeter5(item = item, state = state, onEvent = onEvent)
            Spacer(modifier = Modifier.height(4.dp))

        }
    }
}
@Composable
fun MentalItem(item: Item, state: MyDayState, onEvent: (MyDayEvent)->Unit){
    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    var width by remember { mutableStateOf(0f) }
    Box(modifier = Modifier.fillMaxWidth()
        .drawBehind {
            drawRect(color = androidx.compose.ui.graphics.Color.Blue)
        }
        .pointerInput(Unit){
            detectHorizontalDragGestures { _, dragAmount ->
                println("dragAmount: $dragAmount")
                val originalX = offsetX.value
                println("originalX: $originalX")
                //val newValue = (originalX + dragAmount).coerceIn(0f, width - 50.dp.toPx())
                val newValue = (originalX + dragAmount)
                println("newValue: $newValue")
                offsetX.value = newValue
                println("offsetX.value: ${offsetX.value}")
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = item.heading, fontSize = 24.sp)
            Text(text = "energy: ${item.energy}")
        }
    }

}