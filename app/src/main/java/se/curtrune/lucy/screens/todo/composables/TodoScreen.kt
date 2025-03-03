package se.curtrune.lucy.screens.todo.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.screens.todo.TodoEvent
import se.curtrune.lucy.screens.todo.TodoState
import se.curtrune.lucy.screens.todo.TodoViewModel

@Composable
fun TodoScreen(modifier: Modifier = Modifier,state: TodoState, onEvent: (ItemEvent)->Unit) {
    ItemList(state = state, onEvent = onEvent)
}
@Composable
fun ItemList(state: TodoState, onEvent: (ItemEvent) -> Unit){
    LazyColumn(modifier = Modifier.fillMaxWidth()){
        items(state.items){item->
            BasicItem(item, onEvent = onEvent)
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

