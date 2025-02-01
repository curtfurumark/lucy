package se.curtrune.lucy.screens.dev.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.modules.Repository
import se.curtrune.lucy.screens.item_editor.composables.ItemEditor
import se.curtrune.lucy.screens.todo.composables.ItemList

@Composable
fun RepositoryTest() {
    Card() {
        val repository = LucindaApplication.repository
        var message by remember {
            mutableStateOf("")
        }
        var item by remember {
            mutableStateOf(Item())
        }
        var buttonText by remember {
            mutableStateOf("get item")
        }
        var editorVisible by remember {
            mutableStateOf(false)
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            var itemId by remember {
                mutableStateOf("0")
            }

            OutlinedTextField(
                value = itemId,
                onValueChange = { itemId = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = {
                    Text(text = "item id")
                }
            )
            Text(text = message)
            Button(onClick = {
                println("on button clicked")
                repository.selectItem(itemId.toLong()).also {
                    if (it != null) {
                        item = it
                        editorVisible = true
                    }else{
                        message = "not item with id: $itemId"
                    }
                }
            }) {
                Text(text = buttonText)
            }
        }
        AnimatedVisibility(visible = editorVisible) {
            ItemEditor(item = item, onEvent = {
                repository.delete(item)
            })
        }
    }
}