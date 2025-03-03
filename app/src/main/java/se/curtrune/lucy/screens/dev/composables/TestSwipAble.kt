package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.composables.MyItem
import se.curtrune.lucy.composables.SwipeAbleItem
import se.curtrune.lucy.screens.item_editor.ItemEvent

@Composable
fun TestSwipeAble(){
    val item = Item("hello item")
    var event by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = event)
        SwipeAbleItem(item = item, onEvent = {
            //println("onEvent ${event.toString()}")
            event = it.toString()
        }) { MyItem(item) }
    }
}