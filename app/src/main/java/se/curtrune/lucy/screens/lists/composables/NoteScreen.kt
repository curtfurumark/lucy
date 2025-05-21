package se.curtrune.lucy.screens.lists.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.lists.ListEvent

@Composable
fun NoteScreen(onEvent: (ListEvent) -> Unit){
    var text by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.note),
            color = MaterialTheme.colorScheme.onBackground)
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            value = text,
            onValueChange = {
                text = it
            },
            maxLines = 10,
            label = {
                Text(text = "note")
            }
        )
        Button(onClick = {
            onEvent(ListEvent.SaveNote(text))
        }) {
            Text(text = stringResource(R.string.save))
        }
    }
}
@Composable
@PreviewLightDark
fun PreviewNoteScreen(){
    LucyTheme {
        NoteScreen(onEvent = {})
    }

}