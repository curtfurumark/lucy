package se.curtrune.lucy.screens.medicine.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.screens.medicine.MedicineViewModel


@Composable
fun MedicineScreen(){
    val viewModel: MedicineViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    MedicineList(state = state, onEvent = viewModel::onEvent)
}