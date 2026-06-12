package se.curtrune.lucy.screens.appoinment.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import se.curtrune.lucy.classes.item.Item

@Composable
fun EditDescription(item: Item, onDescriptionChange: (String)->Unit) {
    var description by remember {
        mutableStateOf(item.description)
    }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = {Text("description")},
        value = description,
        onValueChange = {
            description = it
            onDescriptionChange(it)
        }
    )
}