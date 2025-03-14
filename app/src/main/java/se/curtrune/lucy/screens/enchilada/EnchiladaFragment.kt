package se.curtrune.lucy.screens.enchilada

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import se.curtrune.lucy.adapters.ItemAdapter
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.AddItemDialog
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.top_app_bar.TopAppBarEvent
import se.curtrune.lucy.screens.ItemChannel
import se.curtrune.lucy.screens.enchilada.composables.EnchiladaScreen
import se.curtrune.lucy.screens.item_editor.ItemEditorFragment
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.screens.main.MainViewModel
import se.curtrune.lucy.util.Logger

/**
 * A simple [Fragment] subclass.
 * Use the [EnchiladaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnchiladaFragment : Fragment() {
    private var adapter: ItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                var showAddItemDialog by remember{
                    mutableStateOf(false)
                }
                var showMessage by remember {
                    mutableStateOf(false)
                }
                var message by remember {
                    mutableStateOf("")
                }
                val enchiladaViewModel = viewModel<EnchiladaViewModel>()
                val state = enchiladaViewModel.state.collectAsState()
                val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class]
                val filter = mainViewModel.filter.collectAsState()
                LaunchedEffect(filter.value) {
                    println("filter observed: ${filter.value}")
                    enchiladaViewModel.onEvent(TopAppBarEvent.OnSearch(filter.value, false))
                }
                LaunchedEffect(enchiladaViewModel) {
                    enchiladaViewModel.eventFlow.collect{ event->
                        when(event){
                            is ItemChannel.Edit -> { navigate(event.item)}
                            is ItemChannel.ShowAddItemDialog -> {showAddItemDialog = true}
                            is ItemChannel.ShowMessage -> {
                                message = event.message
                                showMessage = true
                            }
                        }
                    }
                }
                LucyTheme {
                    Scaffold( floatingActionButton = { AddItemFab {
                        enchiladaViewModel.onEvent(ItemEvent.ShowAddItemDialog)
                    } }) { padding ->
                        EnchiladaScreen(
                            modifier = Modifier.padding(padding),
                            state = state.value,
                            onEvent = {event->
                                enchiladaViewModel.onEvent(event)
                            })
                    }
                }
                if( showMessage){
                    println("show message")
                }
                if( showAddItemDialog){
                    AddItemDialog(onDismiss = {
                        showAddItemDialog = false
                    }, onConfirm = { item->
                        enchiladaViewModel.onEvent(ItemEvent.InsertItem(item))
                    }
                    , settings = DialogSettings())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            Logger.log("...getArguments != null")
        }
    }

    companion object {
        var VERBOSE: Boolean = false
        fun newInstance(): EnchiladaFragment {
            return EnchiladaFragment()
        }
    }
    //navigate to editor
    private fun navigate(item: Item){
        println("navigate(${item.heading}) to editor")
        val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.updateFragment(
            ItemEditorFragment(
                item
            )
        )
    }
}