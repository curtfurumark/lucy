package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
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
import se.curtrune.lucy.screens.item_editor.ItemEvent

@Composable
fun ItemEditor(item: Item, onEvent: (ItemEvent) -> Unit) {
    var heading by remember {
        mutableStateOf(item.heading)
    }
    var comment by remember {
        mutableStateOf("")
    }
    var energy by remember {
        mutableStateOf(5f)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = item.heading,
            onValueChange = {
                heading = it
            })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = comment,
            onValueChange = {
                comment = it
            })
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                println("on button start click")
            }) {
                Text("start")
            }
            Text(text = "00:00:00", fontSize = 24.sp)
        }
        Slider(value = energy, onValueChange = {
            println("slider on value change $it")
            energy = it
        })
        Button(onClick = {
            onEvent(ItemEvent.Delete(item))
        }){
            Text(text = "delete")
        }
    }
}
