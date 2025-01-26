package se.curtrune.lucy.screens.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.screens.dev.DevState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LucindaControls(state: DevState, onEvent: (String)->Unit) {
    Box() {
        val mentalText by remember {
            mutableStateOf("energy ${state.mental!!.energy}")
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = {
                onEvent("boost me")
            }) {
                Icon(Icons.Filled.Star, contentDescription = "boost me")
            }
            Text(text = mentalText, fontSize = 24.sp)

            IconButton(onClick = {
                onEvent("panic")
            }) {
                Icon(Icons.Filled.Warning, contentDescription = "panic button")
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun Preview(){
    LucindaControls(state = DevState(), onEvent = {})
}
