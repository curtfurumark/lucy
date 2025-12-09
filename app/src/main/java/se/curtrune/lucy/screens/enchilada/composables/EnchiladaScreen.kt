package se.curtrune.lucy.screens.enchilada.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.screens.enchilada.EnchiladaState
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.screens.todo.composables.CheckedItemCard

@Composable
fun EnchiladaScreen(modifier: Modifier = Modifier, state: EnchiladaState, onEvent: (ItemEvent)->Unit){
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.items){ item ->
            CheckedItemCard(item = item, onEvent = onEvent)
            Spacer(modifier = Modifier.height(4.dp))
        }

    }

}