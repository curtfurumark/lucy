package se.curtrune.lucy.screens.projects.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.daycalendar.composables.SwipeBackground
import se.curtrune.lucy.screens.projects.ProjectsEvent

enum class DropDownItems{
    ADD_CHILD, ADD_LIST
}
@Composable
fun ProjectCard(item: Item, modifier: Modifier = Modifier, onEvent: (ProjectsEvent) -> Unit){
    var isDone by remember {
        mutableStateOf(item.isDone)
    }
    var dropDownExpanded by remember {
        mutableStateOf(false)
    }
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if( it == SwipeToDismissBoxValue.EndToStart){
                onEvent(ProjectsEvent.RequestDelete(item))
                true
            }else if (it == SwipeToDismissBoxValue.StartToEnd){
                //onEvent(DayCalendarEvent.ShowPostponeDialog(item))
                true
            }
            false
        }, positionalThreshold = {it * 0.5f}
    )
    SwipeToDismissBox(
        //modifier = Modifier.animateItem(),
        state = swipeState,
        backgroundContent = {
            SwipeBackground(state = swipeState)
        }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        onEvent(ProjectsEvent.OnItemClick(item))
                    },
                    onLongClick = {
                        //onEvent(ProjectsEvent.OnLongItemClick(item))
                        dropDownExpanded = true
                    }
                )
                .padding(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Checkbox(checked = isDone, onCheckedChange = { checked ->
                    item.setIsDone(checked)
                    isDone = checked
                    onEvent(ProjectsEvent.UpdateItem(item))
                })
                Text(
                    text = item.heading,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                if (item.hasChild()) {
                    Text(text = ">", modifier = Modifier.padding(end = 4.dp))
                }
            }
            DropdownMenu(expanded = dropDownExpanded, onDismissRequest = {
                dropDownExpanded = false
            }) {
                for(action in DropDownItems.entries){
                    DropdownMenuItem(
                        text = {Text(text = action.name)},
                        onClick = {
                            onEvent(ProjectsEvent.OnDropDownItemClicked(item, action))
                            dropDownExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
@PreviewLightDark
fun PreviewProjectCard(){
    LucyTheme {
        ProjectCard(item = Item("test"), onEvent = {})
    }

}