package se.curtrune.lucy.screens.todo.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.screens.daycalendar.composables.SwipeBackground
import se.curtrune.lucy.screens.todo.TodoFragmentViewModel

@Composable
fun TodoScreen(){
    val todoViewModel = viewModel<TodoFragmentViewModel>()
    val state = todoViewModel.state.collectAsState()
    state.value.items?.let { ItemList(items = it) }
}
@Composable
fun ItemList(items: List<Item>){
    LazyColumn(modifier = Modifier.fillMaxWidth()){
        items(items){item->
            BasicItem(item)
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

