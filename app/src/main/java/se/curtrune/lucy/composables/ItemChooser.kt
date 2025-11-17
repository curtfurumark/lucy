package se.curtrune.lucy.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item


@Composable
fun ItemChooser(onDismiss: () -> Unit, onItemChosen: (Item) -> Unit){
    var filter by remember { mutableStateOf("") }
    var currentItem by remember { mutableStateOf<Item?>(null) }
    var items by remember { mutableStateOf(listOf<Item>()) }
    val db = LucindaApplication.appModule.repository
    var isLoading by remember { mutableStateOf(false) }
    fun getFilteredItems(filter: String){
        items = db.search(filter)
    }

    Dialog(onDismissRequest = {}){
        Card(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = filter,
                onValueChange = {
                    filter = it
                    getFilteredItems(filter)
                },
                label = { Text("filter") }
            )
            Text(text = "${items.size} items found}")
            items.forEach { item ->
                Text(text = item.heading)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onDismiss) { Text("cancel") }
                Button(onClick = {

                })
                { Text(text = "confirm") }
            }
        }
    }
}