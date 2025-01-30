package se.curtrune.lucy.screens.todo.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.screens.daycalendar.DateEvent
import se.curtrune.lucy.screens.daycalendar.composables.SwipeBackground


@Composable
fun BasicItem(item: Item){
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if( it == SwipeToDismissBoxValue.EndToStart){
                //onEvent(DateEvent.DeleteItem(state.items[index]))
                //Toast.makeText(context, "delete: ${state.items[index]}", Toast.LENGTH_LONG).show()
                true
            }else if (it == SwipeToDismissBoxValue.StartToEnd){
                //onEvent(DateEvent.PostponeItem(state.items[index]))
                //onEvent(DateEvent.ShowPostponeDialog(true))
                //Toast.makeText(context, "postpone: ${state.items[index]}", Toast.LENGTH_LONG).show()
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
            .background(color = Color.DarkGray)) {
            Text(
                modifier = Modifier.padding(2.dp),
                text = item.heading,
                fontSize = 18.sp,
                color = Color.White)
        }
    }
}