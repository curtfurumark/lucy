package se.curtrune.lucy.screens.duration.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.screens.dev.composables.DurationControls
import se.curtrune.lucy.screens.duration.DurationEvent
import se.curtrune.lucy.screens.duration.DurationState

@Composable
fun DurationScreen(state: DurationState, onEvent: (DurationEvent)->Unit){
    Column(modifier = Modifier.fillMaxWidth()) {
        DurationControls(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(8.dp))
        DurationsStatisticsList(state = state)
    }
}