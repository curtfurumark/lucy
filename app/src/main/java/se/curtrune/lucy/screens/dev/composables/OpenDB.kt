package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.dev.DevEvent

@Composable
fun OpenDB(onEvent: (DevEvent)->Unit){
    Card(modifier = Modifier.fillMaxWidth()){
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                onEvent(DevEvent.OpenDB)
            }){
                Text(text = "open db")
            }

        }

    }


}