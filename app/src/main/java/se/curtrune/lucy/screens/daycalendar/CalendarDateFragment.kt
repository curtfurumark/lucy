package se.curtrune.lucy.screens.daycalendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.composables.ItemSettings
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.composables.AddItemDialog
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.LucindaTopAppBar
import se.curtrune.lucy.screens.item_editor.ItemEditorFragment
import se.curtrune.lucy.screens.daycalendar.composables.DayCalendar
import se.curtrune.lucy.screens.main.LucindaViewModel
import java.time.LocalTime


class CalendarDateFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                LucyTheme {
                    // A surface container using the 'background' color from the theme
                    var showAddItemDialog by remember{
                        mutableStateOf(false)
                    }
                    val viewModel = viewModel<DateViewModel>()
                    val state = viewModel.state.collectAsState()
                    val context = LocalContext.current
                    Scaffold(
                        topBar = {
                            LucindaTopAppBar(Mental(), onEvent = {
                                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                            })
                        },
                        floatingActionButton = { AddItemFab {
                            println("add item fab clicked")
                            showAddItemDialog = true
                        }
                        }
                    ) { it ->
                        Surface(
                            modifier = Modifier.fillMaxSize()
                                .padding(it),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            DayCalendar(state = state.value, onEvent = { event->
                                //Toast.makeText(context, event.toString(), Toast.LENGTH_LONG).show()
                                println("DayCalendar.onEvent(${event.toString()})")
                                viewModel.onEvent(event)
                            })
                            if(showAddItemDialog){
                                AddItemDialog(
                                    onDismiss = {showAddItemDialog = false},
                                    onConfirm = {item ->
                                        showAddItemDialog = false
                                        viewModel.onEvent(DateEvent.AddItem(item))
                                    },
                                    settings = ItemSettings(
                                        targetDate = state.value.date,
                                        targetTime = LocalTime.now(),
                                        parent = state.value.currentParent)
                                )
                            }
                            if( state.value.editItem){
                                println("edit item please")
                                state.value.editItem = false
                                state.value.currentItem?.let { it1 -> navigate(it1) }
                            }
                            if(state.value.showStats){
                                Toast.makeText(context, "statistics doncha just love em", Toast.LENGTH_SHORT).show()
                                state.value.showStats = false
                            }
                        }
                    }
                }

            }
        }
    }
    private fun navigate(item: Item){
        println("navigate")
        val mainViewModel = ViewModelProvider(requireActivity())[LucindaViewModel::class.java]
        mainViewModel.updateFragment(
            ItemEditorFragment(
                item
            )
        )
    }
}