package se.curtrune.lucy.screens.projects.composables

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.projects.ProjectTab
import se.curtrune.lucy.screens.projects.ProjectsEvent
import se.curtrune.lucy.screens.projects.ProjectsState

@Composable
fun ProjectTabs(state: ProjectsState, onEvent: (ProjectsEvent) -> Unit){
    val selectedTabIndex = state.tabs.size - 1
    TabRow(selectedTabIndex = 0) {
        state.tabs.forEach{tab->
            println("...tab: $tab")
            Tab(text = { Text(text = tab.parent!!.heading) },
                selected = selectedTabIndex == tab.index,
                onClick = {
                    onEvent(ProjectsEvent.OnTabClick(tab))
                })
        }
    }
}

@Composable
@Preview
@PreviewLightDark
fun PreviewProjectTabs(){
    LucyTheme {
        val tabs = listOf(
            ProjectTab(parent = Item("first"), index = 0),
            ProjectTab(parent = Item("second"), index = 1),
            ProjectTab(parent = Item("third"), index = 2))
        ProjectTabs(state = ProjectsState(tabs =tabs), onEvent = {})
    }
}