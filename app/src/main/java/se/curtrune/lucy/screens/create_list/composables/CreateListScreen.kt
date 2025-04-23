package se.curtrune.lucy.screens.create_list.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.create_list.CreateListEvent
import se.curtrune.lucy.screens.create_list.CreateListState

@Composable
fun CreateListScreen(state: CreateListState, onEvent: (CreateListEvent) -> Unit) {
    //var items by remember { mutableStateOf(listOf("")) }
    println("...CreateListScreen(number of items: ${state.items.size})")
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "create list", color = MaterialTheme.colorScheme.onBackground)
        Text(
            text = "focus index: ${state.focusIndex}",
            color = MaterialTheme.colorScheme.onBackground)
        Text(
            text = "items size: ${state.items.size}"  ,
            color = MaterialTheme.colorScheme.onBackground)
        Text(
            text = "parent: ${state.parent!!.heading}",
            color = MaterialTheme.colorScheme.onBackground)
        state.items.forEachIndexed { index, item ->
            BulletListItem(
                text = item,
                onTextChange = { newText ->
                    //items = items.toMutableList().also { it[index] = newText }
                    //onEvent(CreateListEvent.OnUpdate(index, newText))
                },
                onEnter = { heading->
                    //items = items.toMutableList().also { it.add(index + 1, "") }
                    onEvent(CreateListEvent.OnEnter(text = heading, index=index))
                },
                setFocus = state.focusIndex == index
            )
        }
    }
}

@Composable
fun BulletListItem(text: String, onTextChange: (String) -> Unit, onEnter: (String) -> Unit, setFocus: Boolean = false) {
    val focusRequester = remember { FocusRequester() }
    var heading by remember {
        mutableStateOf("") }

    val scope = rememberCoroutineScope()
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(8.dp)) {
            //Text(text = "•", modifier = Modifier.padding(end = 8.dp))
            TextField(
                value = heading,
                onValueChange = { heading = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onKeyEvent { event ->
                        if (event.key == Key.Enter) {
                            heading = heading.trim()
                            onEnter(heading); true
                        } else false
                    },
            )
        }
    }
    LaunchedEffect(setFocus) {
        println("...LaunchedEffect(setFocus: $setFocus)")
        focusRequester.requestFocus()
    }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@Composable
@Preview
@PreviewLightDark
fun PreviewBulletList() {
    LucyTheme {
        val state = CreateListState().also {
            it.items = listOf("item1", "item2", "item3")
            it.parent =Item("hello")
        }
        CreateListScreen(state = state, onEvent = {})
    }
}