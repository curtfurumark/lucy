package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.dev.DevEvent

@Composable
fun HolidaysTest(onEvent: (DevEvent)->Unit){
    Card(modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier.fillMaxWidth()){
            Text(text = "test of get holidays")
        }
        Row(modifier = Modifier.fillMaxWidth()){
            Button(onClick = {

            }){
                Text(text = "get holidays")
            }
        }

    }
}