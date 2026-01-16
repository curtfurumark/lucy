package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.Type

@Composable
fun TypeCard(type: Type, onTypeChanged: (Type)->Unit){
    var showDropDown by remember { mutableStateOf(false) }
    var typeName by remember { mutableStateOf(type.name) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {showDropDown = !showDropDown}) {
        Text(text = "type: $typeName"
        ,modifier = Modifier.fillMaxWidth().padding(4.dp))
    }
    if( showDropDown) {
        DropdownMenu(expanded = showDropDown, onDismissRequest = { showDropDown = false }) {
            Type.entries.forEach {
                DropdownMenuItem(text = { Text(text = it.name) }, onClick = {
                    typeName = it.name
                    onTypeChanged(it)
                    showDropDown = false
                })
            }
        }
    }
}