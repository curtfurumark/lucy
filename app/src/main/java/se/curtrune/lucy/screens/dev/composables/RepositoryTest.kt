package se.curtrune.lucy.screens.dev.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.item_editor.composables.ItemEditor
import java.time.LocalDate

@Composable
fun RepositoryTest() {
    Card() {
        val repository = LucindaApplication.appModule.repository
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
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    println("on button clicked")
                    repository.selectItem(itemId.toLong()).also {
                        if (it != null) {
                            item = it
                            editorVisible = true
                        } else {
                            message = "not item with id: $itemId"
                        }
                    }
                }) {
                    Text(text = buttonText)
                }
                Button(onClick = {
                    val child = repository.selectItem(itemId.toLong())
                    if( child != null) {
                        println("got item: ${child.heading}")
                        repository.touchParents(child)
                    }
                }) {
                    Text(text = "touch parents")
                }
                Button(onClick = {
                    val items = repository.selectItems(LocalDate.now())
                    message = "found ${items.size} items"
                    items.forEach{
                        println("item: ${it.heading}")
                    }
                }) {
                    Text(text = "select today")
                }
            }

        }
        AnimatedVisibility(visible = editorVisible) {
            ItemEditor(item = item, onEvent = {
                repository.delete(item)
            })
        }
    }
}

@Composable
@Preview
fun PreviewRepositoryTest(){
    LucyTheme {
        RepositoryTest()
    }

}