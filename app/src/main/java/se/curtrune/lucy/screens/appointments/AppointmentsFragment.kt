package se.curtrune.lucy.screens.appointments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.add_item.AddItemDialog
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.screens.item_editor.ItemEditorFragment
import se.curtrune.lucy.screens.appointments.composables.AppointmentsScreen
import se.curtrune.lucy.screens.main.MainViewModel
import se.curtrune.lucy.util.Logger

class AppointmentsFragment : Fragment() {
    init {
        if (VERBOSE) Logger.log("AppointmentsFragment()")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                val appointmentsViewModel = viewModel<AppointmentsViewModel>()
                val state = appointmentsViewModel.state.collectAsState()
                val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class]
                val filter = mainViewModel.filter.collectAsState()
                LaunchedEffect(filter.value) {
                    appointmentsViewModel.onEvent(AppointmentEvent.Filter(filter.value))
                }
                var showAddAppointmentDialog by remember{
                    mutableStateOf(false)
                }
                var showMessage by remember {
                    mutableStateOf(false)
                }
                val context = LocalContext.current
                LaunchedEffect(appointmentsViewModel) {
                    appointmentsViewModel.eventFlow.collect{ event->
                        when(event){
                            is UIEvent.EditItem -> {navigateToEditor(event.item)}
                            is UIEvent.ShowMessage -> TODO()
                            is UIEvent.ShowAddItemDialog -> {showAddAppointmentDialog = true}
                        }
                    }
                }
                LucyTheme {
                    Scaffold(
                        floatingActionButton = {
                            AddItemFab {
                                appointmentsViewModel.onEvent(AppointmentEvent.ShowAddAppointmentDialog)
                            }
                        }
                    ) { padding->
                        Surface(modifier = Modifier.padding(padding)) {
                            AppointmentsScreen(state = state.value, onEvent = { event ->
                                appointmentsViewModel.onEvent(event)
                            })
                        }
                    }
                    if( showAddAppointmentDialog){
                        AddItemDialog(onDismiss = {
                            showAddAppointmentDialog = false
                        },
                            onConfirm ={ item->
                                appointmentsViewModel.onEvent(AppointmentEvent.InsertAppointment(item))
                            }
                            , appointmentsViewModel.dialogSettings)
                    }
                    if( showMessage) {
                        Toast.makeText(context, "", Toast.LENGTH_LONG).show()
                        showMessage = false
                    }
                }
            }
        }
    }

    private fun navigateToEditor(item: Item) {
        println("...editAppointment(${item.heading})")
        val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.updateFragment(
            ItemEditorFragment(
                item
            )
        )
    }
    private fun showMessage(message: String){
        Toast.makeText(requireContext(),message, Toast.LENGTH_LONG ).show()
    }

    companion object {
        var VERBOSE: Boolean = false
    }
}