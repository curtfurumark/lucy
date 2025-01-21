package se.curtrune.lucy.screens.duration.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.duration.DurationEvent
import se.curtrune.lucy.screens.duration.DurationState

@Composable
fun DurationScreen(state: DurationState, onEvent: (DurationEvent)->Unit){
    Column(modifier = Modifier.fillMaxWidth()){
        PeriodDropDown(onEvent = onEvent)
        DurationList(state = state, onEvent = onEvent)
    }

}