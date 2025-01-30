package se.curtrune.lucy.screens.appointments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.dialogs.EventDialog
import se.curtrune.lucy.screens.item_editor.ItemEditorFragment
import se.curtrune.lucy.screens.appointments.composables.AppointmentsScreen
import se.curtrune.lucy.screens.main.LucindaViewModel
import se.curtrune.lucy.util.Logger

class AppointmentsFragment : Fragment() {
    private var buttonAdd: FloatingActionButton? = null
    private var mainViewModel: LucindaViewModel? = null
    private var appointmentsViewModel: AppointmentsViewModel? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    init {
        if (VERBOSE) Logger.log("AppointmentsFragment()")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logger.log("AppointmentsFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)")
        val view = inflater.inflate(R.layout.appintments_fragment, container, false)
        //initViewModel()
        //initComponents(view)
        requireActivity().title = getString(R.string.appointments)
        //initSwipe()
        //initRecycler()
        //initListeners()
        initContent(view )
        return view
    }

    private fun addAppointment() {
        if (VERBOSE) Logger.log("...addAppointment()")
        val dialog = EventDialog()
        dialog.setCallback { item: Item? ->
            Logger.log("...onNewAppointment(Item item")
        }
        dialog.show(parentFragmentManager, "add appointment")
    }

    private fun initContent(view: View ){
        val composeView = view.findViewById<ComposeView>(R.id.appointmentsFragment_composeView)
        composeView.setContent {
            val appointmentsViewModel = viewModel<AppointmentsViewModel>()
            val state = appointmentsViewModel.state.collectAsState()
            LaunchedEffect(appointmentsViewModel) {
                appointmentsViewModel.eventFlow.collect{ event->
                    when(event){
                        is UIEvent.EditItem -> {navigateToEditor(event.item)}
                        is UIEvent.ShowMessage -> TODO()
                    }
                }
            }
            LucyTheme {
                Scaffold(
                    floatingActionButton = {}
                ) { padding->
                    Surface(modifier = Modifier.padding(padding)) {
                        AppointmentsScreen(state = state.value, onEvent = { event ->
                            appointmentsViewModel.onEvent(event)
                        })
                    }
                }
            }
        }
    }
    private fun navigateToEditor(item: Item) {
        println("...editAppointment(${item.heading})")
        val mainViewModel = ViewModelProvider(requireActivity())[LucindaViewModel::class.java]
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