package se.curtrune.lucy.screens.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NoteScreen(){
    val viewModel: NoteViewModel = viewModel()
    Column(modifier = Modifier.fillMaxSize()) {
        var title by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }
        Text(text ="add a note")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },)
            Icon(
                modifier = Modifier.clickable(onClick = {
                    viewModel.onEvent(NoteEvent.OnSave(title, content))
                    title = ""
                    content = ""
                }),
                imageVector = Icons.Default.Check,
                contentDescription ="save")
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = content,
            onValueChange = {content = it},
            label = { Text(text = "content") })
    }
}