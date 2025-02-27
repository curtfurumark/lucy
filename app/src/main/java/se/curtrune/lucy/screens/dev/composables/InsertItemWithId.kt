package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.screens.dev.DevEvent

@Composable
fun InsertItemWithID(onEvent: (DevEvent)->Unit){
    Card(modifier = Modifier.fillMaxWidth()){
        var itemID by remember{
            mutableStateOf("")
        }
        var heading by remember {
            mutableStateOf("")
        }
        Column() {
            Text(text = "insert item with id", fontSize = 24.sp)
            OutlinedTextField(
                value = itemID,
                onValueChange = { itemID = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = {
                    Text(text = "item id")
                }
            )
            OutlinedTextField(
                value = heading,
                onValueChange = { heading = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = {
                    Text(text = "heading")
                }
            )
            Button(onClick = {
                println("onClickity click")
                val item = Item()
                item.heading = heading
                item.id = itemID.toLong()
                itemID = ""
                heading = ""
                onEvent(DevEvent.InsertItemWithID(item))

            }) {
                Text(text = "insert with item id")

            }
        }
    }
}