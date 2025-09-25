package se.curtrune.lucy.screens.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.composables.DialogSettings
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.top_app_bar.TopAppBarEvent
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.screens.main.MainViewModel
import se.curtrune.lucy.screens.todo.composables.TodoScreen

class TodoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                val todoViewModel = viewModel<TodoViewModel>()
                val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class]
                val state = todoViewModel.state.collectAsState()
                val filter = mainViewModel.searchFilter.collectAsState()
                LaunchedEffect(filter.value) {
                    todoViewModel.onEvent(TopAppBarEvent.OnSearch(filter.value.filter, filter.value.everywhere))
                }
                var showProgressBar by remember{
                    mutableStateOf(false)
                }
                var showAddItemDialog by remember {
                    mutableStateOf(false)
                }
                @Composable
                fun ShowAddItemDialog(settings: DialogSettings) {
/*                    AddItemDialog(
                        onDismiss = {
                            showAddItemDialog = false
                        },
                        onConfirm = { item ->
                            todoViewModel.onEvent(ItemEvent.InsertItem(item))
                            showAddItemDialog = false
                        }, settings = settings)*/
                }
                LaunchedEffect(todoViewModel) {
                    todoViewModel.eventFlow.collect{ event->
                        when(event){
                            is ChannelEvent.Edit -> {
                                navigateToEditor(event.item)
                            }
                            is ChannelEvent.ShowMessage ->{
                                println(event.message)
                                Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()
                            }

                            is ChannelEvent.ShowProgressBar -> {
                                showProgressBar = event.show
                                println("...showProgress bar: ${event.show}")
                            }

                            ChannelEvent.ShowAddItemDialog -> {
                                showAddItemDialog = true
                                //ShowAddItemDialog(settings = DialogSettings())
                            }
                        }
                    }
                }
                LucyTheme {
                    Scaffold(
                        floatingActionButton = { AddItemFab {
                            todoViewModel.onEvent(ItemEvent.ShowAddItemDialog)
                        }}
                    ) {padding->
                        TodoScreen(modifier = Modifier.padding(padding), state = state.value, onEvent = { event ->
                            todoViewModel.onEvent(event)
                        })
                        if (showProgressBar) {
                            CircularProgressIndicator()
                        }
/*                        if (showAddItemDialog) {
                            AddItemDialog(onDismiss = {
                                showAddItemDialog = false
                            },
                                onConfirm = { item ->
                                    todoViewModel.onEvent(ItemEvent.InsertItem(item))
                                    showAddItemDialog = false
                                }, settings = state.value.newItemSettings)
                        }*/

                    }
                }
            }
        }
    }


    private fun navigateToEditor(item: Item) {
        println("...navigateToEditor(${item.heading})")
        val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        throw Exception("not implemented, deprecated")
/*        mainViewModel.updateFragment(
            ItemEditorFragment(
                item
            )
        )*/
    }





    companion object {
        var VERBOSE: Boolean = false
    }
}