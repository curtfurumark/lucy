package se.curtrune.lucy.screens.appoinment.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import se.curtrune.lucy.classes.item.Item

@Composable
fun CheckableItem(item: Item, onCheckChange: (Item)->Unit    ){
    var item by remember {
        mutableStateOf(item)
    }
    var checked by remember {
        mutableStateOf(item.isDone)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically){
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = it
                item.isDone = it
                onCheckChange(item)
            }
        )
        Text(text = item.heading)
    }
}