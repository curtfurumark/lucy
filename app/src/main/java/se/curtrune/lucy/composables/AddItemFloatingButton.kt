package se.curtrune.lucy.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun AddItemFloatingActionButton(onClick: ()->Unit){
    FloatingActionButton(onClick = { onClick()}) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "add event")
    }
}