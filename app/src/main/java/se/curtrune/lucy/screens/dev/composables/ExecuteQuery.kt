package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import se.curtrune.lucy.screens.dev.DevEvent

@Composable
fun ExecuteQuery(onEvent: (DevEvent)->Unit){
    var query by remember {
        mutableStateOf("UPDATE items SET estimate = null WHERE estimate != null")
    }
    Card(modifier =  Modifier.fillMaxWidth()){
        Column(modifier = Modifier.fillMaxWidth()){
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = query, onValueChange = {
                    query = it
                })
            }
            Row(modifier = Modifier.fillMaxWidth()){
                Button(onClick = {
                    onEvent(DevEvent.RunQuery(query))
                }) {
                    Text(text = "run query")
                }
            }
        }
    }
}