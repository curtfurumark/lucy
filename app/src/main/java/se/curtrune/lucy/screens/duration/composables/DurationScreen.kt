package se.curtrune.lucy.screens.duration.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.screens.dev.composables.DurationControls
import se.curtrune.lucy.screens.duration.DurationEvent
import se.curtrune.lucy.screens.duration.DurationState
import se.curtrune.lucy.screens.duration.DurationViewModel

@Composable
fun DurationScreen( onEvent: (DurationEvent)->Unit){
    val viewModel: DurationViewModel = viewModel()
    val state =   viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxWidth()) {
        DurationControls(state = state.value, onEvent = {
            viewModel.onEvent(it)
        })
        Spacer(modifier = Modifier.height(8.dp))
        DurationsStatisticsList(state = state.value)
    }
}