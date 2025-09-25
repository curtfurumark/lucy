package se.curtrune.lucy.screens.projects.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.screens.projects.ProjectsEvent
import se.curtrune.lucy.screens.projects.ProjectsState

@Composable
fun ProjectsList(state: ProjectsState, onEvent: (ProjectsEvent) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        ProjectTabs(state = state, onEvent = onEvent)
        //println("after projects tab")
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(state.items) { item ->
                ProjectItem(item = item, onEvent = onEvent)
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    }
}