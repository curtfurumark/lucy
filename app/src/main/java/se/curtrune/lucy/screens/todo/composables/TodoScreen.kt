package se.curtrune.lucy.screens.todo.composables

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.add_item.DefaultItemSettings
import se.curtrune.lucy.screens.edit.ItemEvent
import se.curtrune.lucy.screens.navigation.Route.ItemEditorNavKey
import se.curtrune.lucy.screens.todo.TodoChannel
import se.curtrune.lucy.screens.todo.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    navigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier) {
    val viewModel: TodoViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val eventFlow = viewModel.channel
    val context = LocalContext.current
    var showAddItemDialog by remember { mutableStateOf(false) }
    var showProgressBar by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            when (event) {
                is TodoChannel.AddList->{
                    //navigate(EditListNavKey(it.id))
                }
                is TodoChannel.Edit -> {
                    navigate(ItemEditorNavKey(event.item))
                }
                is TodoChannel.Navigate ->{
                    navigate(event.navKey)
                }
                is TodoChannel.ShowAddItemDialog -> {
                    showAddItemDialog = true
                }
                is TodoChannel.ShowMessage -> {
                    println("show message")
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is TodoChannel.ShowProgressBar -> {
                    println("show progress bar")
                    showProgressBar = event.show
                }
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showAddItemDialog = true
            }){
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }

        }
    ) { padding ->
        ItemList(modifier = modifier.padding(padding), state = state,
            onEvent = {
                viewModel.onEvent(it)
            },
            sortEvent = {
                viewModel.onEvent(it)
            })
    }
    if (showAddItemDialog) {
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
    if(showProgressBar){
        println("show progressbar")
    }
}


