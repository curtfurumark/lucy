package se.curtrune.lucy.composables.dialogs

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
import se.curtrune.lucy.classes.item.Item

@Composable
fun AddChildDialog(parentId: Long, onDismiss: ()->Unit, onAdd: (Item)->Unit){
    Dialog(onDismissRequest = onDismiss) {
        var heading by remember {
            mutableStateOf("")
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(text = "add checkable item")
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = heading,
                onValueChange = {
                    heading = it
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    val item = Item(heading)
                    item.parentId = parentId
                    onAdd(item) }) {
                    Text(text = "add")
                }
                Button(onClick = { onDismiss() }) {
                    Text(text = "cancel")
                }
            }
        }
    }
}