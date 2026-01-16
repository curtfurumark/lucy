package se.curtrune.lucy.screens.item_editor.composables

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import se.curtrune.lucy.classes.item.Item
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.screens.item_editor.ItemEditorChannel
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.screens.item_editor.ItemEditorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditorScreen(item: Item, onSave:(Item)->Unit ){
    println("ItemEditorScreen($item)")
    val viewModel: ItemEditorViewModel = viewModel(){
        ItemEditorViewModel.factory(item).create(ItemEditorViewModel::class.java)
    }
    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val topAppBarState = TopAppbarModule.topAppBarState.collectAsState()
    var showAddChildDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            AddItemFab(onAddClick = {
                println("on add click")
                viewModel.onEvent(ItemEvent.ShowAddItemDialog)
            })
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
    ) {padding->
        ItemEditor(
            modifier = Modifier.padding(padding),
            item = item,
            categories = state.categories,
            onEvent = {
                viewModel.onEvent(it)
            },
            onSave = onSave
        )
    }
    LaunchedEffect(viewModel) {
        viewModel.channel.collect{ event->
            when(event){
                is ItemEditorChannel.ShowAddChildDialog ->{
                    showAddChildDialog = true
                }
                is ItemEditorChannel.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    if( showAddChildDialog){
        println("show add child dialog toast")
        Toast.makeText(context, "show add child dialog", Toast.LENGTH_SHORT).show()
        AddItemBottomSheet(
            defaultItemSettings = viewModel.defaultItem,
            onDismiss = {
                showAddChildDialog = false
            },
            onSave = {
                viewModel.onEvent(ItemEvent.InsertChild(it))
                showAddChildDialog = false
            }
        )
    }
}