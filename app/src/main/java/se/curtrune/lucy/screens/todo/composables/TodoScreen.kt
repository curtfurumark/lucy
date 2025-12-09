package se.curtrune.lucy.screens.todo.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.add_item.DefaultItemSettings
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.screens.navigation.ItemEditorNavKey
import se.curtrune.lucy.screens.todo.ChannelEvent
import se.curtrune.lucy.screens.todo.TodoState
import se.curtrune.lucy.screens.todo.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(navigate: (NavKey) -> Unit) {
    val viewModel: TodoViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val eventFlow = viewModel.channel
    val context = LocalContext.current
    var showAddItemDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            when (event) {
                is ChannelEvent.AddList->{
                    //navigate(EditListNavKey(it.id))

                }
                //is TodoEvent.Navigate -> navigate(event.navKey)
                is ChannelEvent.Edit -> {
                    navigate(ItemEditorNavKey(event.item))
                }
                is ChannelEvent.Navigate ->{
                    navigate(event.navKey)
                }

                is ChannelEvent.ShowAddItemDialog -> {
                    showAddItemDialog = true
                }

                is ChannelEvent.ShowMessage -> {
                    println("show message")
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is ChannelEvent.ShowProgressBar -> {
                    println("show progress bar")
                }

            }
        }
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val topAppBarState = TopAppbarModule.topAppBarState.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {}){
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }

        },

        topBar = {
            FlexibleTopBar(
                scrollBehavior = scrollBehavior,
                content = {
                    LucindaTopAppBar(
                        state = topAppBarState.value,
                        onEvent = { appBarEvent ->
                            println("appBarEvent $appBarEvent")
                            //mainViewModel.onEvent(appBarEvent)
                        })
                }, onEvent = { event ->
                    println("onEvent $event")
                }
            )
        }

    ) { padding ->
        ItemList(modifier = Modifier.padding(padding), state = state, onEvent = {
            viewModel.onEvent(it)
        })
    }
    if (showAddItemDialog) {
        println("show add item dialog")
        AddItemBottomSheet(
            defaultItemSettings = DefaultItemSettings(),
            onDismiss = {
                showAddItemDialog = false
            },
            onSave = {
                viewModel.onEvent(ItemEvent.InsertItem(it))
                showAddItemDialog = false
            })
    }
}
@Composable
fun ItemList(modifier: Modifier = Modifier, state: TodoState, onEvent: (ItemEvent) -> Unit){
    LazyColumn(modifier = modifier.fillMaxWidth()){
        items(state.items){item->
            CheckedItemCard(item, onEvent = onEvent)
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

