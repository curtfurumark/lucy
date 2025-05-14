package se.curtrune.lucy.screens.lists.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.screens.lists.ListEvent
import se.curtrune.lucy.screens.lists.ListState

@Composable
fun ListScreen(state: ListState, onEvent: (ListEvent) -> Unit, modifier: Modifier = Modifier){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly) {
        Text(
            text = "List Screen",
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            color = MaterialTheme.colorScheme.onBackground)
        Button(onClick = {
            onEvent(ListEvent.CreateList)
        }) {
            Text(text = "create list")
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewListScreen(){
    LucyTheme {
        ListScreen(state = ListState(), onEvent = {})
    }
}