package se.curtrune.lucy.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.screens.daycalendar.composables.SwipeBackground
import se.curtrune.lucy.screens.item_editor.ItemEvent

@Composable
fun SwipeAbleItem(item: Item, onEvent: (ItemEvent)->Unit, content: @Composable()()->Unit){
    val context = LocalContext.current
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if( it == SwipeToDismissBoxValue.EndToStart){
                onEvent(ItemEvent.Delete(item))
                //Toast.makeText(context, "delete: ${item.heading}", Toast.LENGTH_LONG).show()
                true
            }else if (it == SwipeToDismissBoxValue.StartToEnd){
                //onEvent(ItemEvent.(item))
                //onEvent(DateEvent.ShowPostponeDialog(true))
                //Toast.makeText(context, "postpone: ${item.heading}", Toast.LENGTH_LONG).show()
                true
            }
            false
        }, positionalThreshold = {it * 0.25f}
    )
    SwipeToDismissBox(
        state = swipeState,
        backgroundContent = { SwipeBackground(state = swipeState) }
    ) {
        MyItem(item = item)
    }
}
@Composable
fun MyItem(item: Item){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = item.heading)
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewSwipeAbleItem(){
    SwipeAbleItem(
        item = Item("i am an item"),
        onEvent = {},
        content = { MyItem(item = Item("hello i am item")) }
    )
}