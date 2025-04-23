package se.curtrune.lucy.screens.projects.composables

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Tab
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
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
    val lazyListState = rememberLazyListState()
    LaunchedEffect(state.tabs.size){
        if (state.tabs.isNotEmpty()){
            lazyListState.animateScrollToItem(state.tabs.size -1)
        }
    }


    println("...selectedTabIndex $selectedTabIndex")
/*    ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
        state.tabs.forEach{ tab->
            println("...tab: $tab")
            Tab(text = { Text(text = tab.parent!!.heading) },
                selected = selectedTabIndex == tab.index,
                onClick = {
                    onEvent(ProjectsEvent.OnTabClick(tab))
                })
        }
    }*/
    LazyRow(state = lazyListState){
        items(state.tabs){ tab->
            Tab(
                text = { tab.parent?.let { Text(text = it.heading) } },
                selectedContentColor = if(selectedTabIndex == tab.index) Color.Green else Color.Gray,
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
            ProjectTab(parent = Item("third"), index = 2),
            ProjectTab(parent = Item("fourth"), index = 3))
        ProjectTabs(state = ProjectsState(tabs =tabs), onEvent = {})
    }
}