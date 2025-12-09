package se.curtrune.lucy.screens.todo.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.daycalendar.composables.ContextActions
import se.curtrune.lucy.screens.daycalendar.composables.SwipeBackground
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.screens.medicine.composable.DropdownItem


@Composable
fun CheckedItemCard(item: Item, onEvent: (ItemEvent)->Unit){
    val context = LocalContext.current
    var showContextMenu by remember { mutableStateOf(false) }
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if( it == SwipeToDismissBoxValue.EndToStart){
                onEvent(ItemEvent.Delete(item))
                //Toast.makeText(context, "delete: ${item.heading}", Toast.LENGTH_LONG).show()
                true
            }else if (it == SwipeToDismissBoxValue.StartToEnd){
                //onEvent(TodoEvent.Postpone(item))
                //onEvent(DateEvent.ShowPostponeDialog(true))
                //Toast.makeText(context, "postpone: ${item.heading}", Toast.LENGTH_LONG).show()
                true
            }
            false
        }, positionalThreshold = {it * 0.25f}
    )
    SwipeToDismissBox(
        state = swipeState,
        backgroundContent = {SwipeBackground(state = swipeState)}
    ) {
        Card(modifier = Modifier.fillMaxWidth()
            .background(color = Color.DarkGray)
            .pointerInput(true) {
                detectTapGestures(
                    onLongPress = {
                        showContextMenu = true
                        //pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                    },
                    onTap = {
                        println("on tap, i am item ${item.heading}")
                        if( item.hasChild()) {
                            onEvent(ItemEvent.ShowChildren(item))
                        }else{
                            onEvent(ItemEvent.Edit(item))
                        }
                    }
                )
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = item.isDone, onCheckedChange = {
                    println("on checkbox checked $it")
                    item.setIsDone(it)
                    onEvent(ItemEvent.Update(item))
                })
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = item.heading,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            if (showContextMenu){
                ContextActions.entries.forEach{
                    DropdownItem(it.name, onClick = { action->
                        println("action: $action")
                        when(action){
                            ContextActions.VIEW_STATS.name ->{
                                //onEvent(DayEvent.ShowStats(item))
                            }
                            ContextActions.EDIT.name ->{
                                onEvent(ItemEvent.Edit(item))
                            }
                            ContextActions.DUPLICATE.name ->{
                                //onEvent(ItemEvent.Duplicate(item))
                            }
                            ContextActions.ADD_LIST.name ->{
                                onEvent(ItemEvent.AddChildren(item))
                            }
                        }
                        showContextMenu = false
                    })
                }
            }
        }
    }
}