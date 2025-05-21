package se.curtrune.lucy.screens.lists.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.screens.create_list.composables.CreateListScreen
import se.curtrune.lucy.screens.lists.ListEvent
import se.curtrune.lucy.screens.lists.ListState

@Composable
fun ListScreen(state: ListState, onEvent: (ListEvent) -> Unit, modifier: Modifier = Modifier){
    AnimatedVisibility(state.optionsVisible) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "List Screen",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                color = MaterialTheme.colorScheme.onBackground
            )
            Button(onClick = {
                onEvent(ListEvent.CreateList)
            }) {
                Text(text = stringResource(R.string.create_list))
            }
            Button(onClick = {
                onEvent(ListEvent.CreateNote)
            }) {
                Text(text = stringResource(R.string.create_note))

            }
        }
    }
    AnimatedVisibility(state.listVisible) {
        //EditableBulletList()
        //CreateListScreen()
    }
    AnimatedVisibility(state.noteVisible) {
        NoteScreen(onEvent = onEvent)
    }
}

@Composable
@PreviewLightDark
fun PreviewListScreen(){
    LucyTheme {
        ListScreen(state = ListState(), onEvent = {})
    }
}