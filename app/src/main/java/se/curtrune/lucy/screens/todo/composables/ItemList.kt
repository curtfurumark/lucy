package se.curtrune.lucy.screens.todo.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.composables.item.CheckableItemCard
import se.curtrune.lucy.screens.edit.ItemEvent
import se.curtrune.lucy.screens.timeline.composables.SortBar
import se.curtrune.lucy.screens.timeline.composables.SortEvent
import se.curtrune.lucy.screens.todo.TodoState

@Composable
fun ItemList(
    modifier: Modifier = Modifier,
    state: TodoState,
    onEvent: (ItemEvent) -> Unit,
    sortEvent: (SortEvent) -> Unit) {
    LazyColumn(modifier = modifier.fillMaxWidth()){
        item{
            SortBar(onEvent = sortEvent)
        }
        items(state.items){item->
            CheckableItemCard(
                item,
                onEvent = onEvent,
                onCheckValueChanged = {})
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}