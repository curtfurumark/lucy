package se.curtrune.lucy.screens.medicine.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.classes.mental_fragment.EditItemsScreen
import se.curtrune.lucy.screens.medicine.MedicineChannelEvent
import se.curtrune.lucy.screens.medicine.MedicineEvent
import se.curtrune.lucy.screens.medicine.MedicineViewModel
import se.curtrune.lucy.screens.navigation.ItemEditorNavKey


@Composable
fun MedicineScreen(modifier: Modifier = Modifier,
                   navigate: (NavKey) -> Unit)
{
    val viewModel: MedicineViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    var showAddItemDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddItemDialog = true
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        }

    ) {
        MedicineList(state = state, onEvent = viewModel::onEvent, modifier = modifier.padding(it))
    }
    LaunchedEffect(viewModel) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is MedicineChannelEvent.Edit -> {
                    navigate(ItemEditorNavKey(event.item))
                }
                MedicineChannelEvent.ShowAdverseEffectsDialog -> TODO()
                is MedicineChannelEvent.ShowMessage -> TODO()
                is MedicineChannelEvent.ShowProgressBar -> TODO()
            }
        }
    }
    if( showAddItemDialog) {
        MedicineDialog(
            onDismiss = { showAddItemDialog = false },
        ) {
            showAddItemDialog = false
            viewModel.onEvent(MedicineEvent.Insert(it))
        }
    }
}