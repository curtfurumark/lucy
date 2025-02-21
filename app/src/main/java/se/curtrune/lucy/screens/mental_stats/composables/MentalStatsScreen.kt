package se.curtrune.lucy.screens.mental_stats.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.mental_stats.MentalStatsEvent
import se.curtrune.lucy.screens.mental_stats.MentalStatsState


@Composable
fun MentalStatsScreen(state: MentalStatsState, onEvent: (MentalStatsEvent)->Unit){
    Column(modifier = Modifier.fillMaxWidth()){
        Text(text = "mental stats screen")
        MentalControls(state = state, onEvent = onEvent)
        //MentalLists()
    }

}