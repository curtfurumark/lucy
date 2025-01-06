package se.curtrune.lucy.activities.kotlin.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun AddItemFab(onAddClick: ()->Unit){
    FloatingActionButton(
        //modifier = Modifier.padding(16.dp).align(Alignment.Bottom),
        onClick = {
            onAddClick()
            //println("add button clicked")
        },
    ) {
        Icon(Icons.Filled.Add, "add medicine item")
    }
}