package se.curtrune.lucy.screens.medicine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.item_editor.ItemEditorFragment
import se.curtrune.lucy.screens.main.MainViewModel
import se.curtrune.lucy.screens.medicine.composable.AddMedicineFab
import se.curtrune.lucy.screens.medicine.composable.AdverseEffectDialog
import se.curtrune.lucy.screens.medicine.composable.MedicineDialog
import se.curtrune.lucy.screens.medicine.composable.MedicineList

class MedicineFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                requireActivity().title = getString(R.string.medicines)
                val medicineViewModel = viewModel<MedicineViewModel>()
                val state = medicineViewModel.state.collectAsState()
                var showAdverseEffectsDialog by remember{
                    mutableStateOf(false)
                }
                LaunchedEffect(medicineViewModel) {
                    medicineViewModel.eventFlow.collect{ event->
                        println("event $event ")
                        when(event){
                            is MedicineChannelEvent.Edit -> {
                                navigateToEditor(event.item)
                            }
                            is MedicineChannelEvent.ShowMessage -> {

                            }
                            is MedicineChannelEvent.ShowProgressBar -> {

                            }
                            is MedicineChannelEvent.ShowAdverseEffectsDialog -> {
                                println("show adverse effect dialog")
                                showAdverseEffectsDialog = true
                            }
                        }
                    }
                }
                if (state.value.showAddMedicineDialog) {
                    MedicineDialog(onDismiss = {
                        medicineViewModel.onEvent(MedicineEvent.ShowAddMedicineDialog(false))
                    }, onConfirm = { item ->
                        medicineViewModel.onEvent(MedicineEvent.Insert(item))
                        medicineViewModel.onEvent(MedicineEvent.ShowAddMedicineDialog(false))
                    })
                }
                if (state.value.errorMessage.isNotEmpty()) {
                    println("show error dialog")
                }
                if (state.value.message.isNotEmpty()) {
                    println("show message dialog")
                }
                LucyTheme {
                    Scaffold(floatingActionButton = {
                        AddMedicineFab(onAddClick = {
                            medicineViewModel.onEvent(MedicineEvent.ShowAddMedicineDialog(true))
                        })
                    }) {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MedicineList(state = state.value, onEvent = { event ->
                                medicineViewModel.onEvent(event)
                            })
                        }
                    }
                }
                if(showAdverseEffectsDialog){
                    println("show adverse effects dialog is true")
                    AdverseEffectDialog(onDismiss = {
                        showAdverseEffectsDialog = false
                    })
                }

            }
        }
    }

    private fun navigateToEditor(item: Item) {
        println("...navigateToEditor(${item.heading})")
        val mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.updateFragment(
            ItemEditorFragment(
                item
            )
        )
    }
}


