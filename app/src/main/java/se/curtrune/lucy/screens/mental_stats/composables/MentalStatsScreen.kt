package se.curtrune.lucy.screens.mental_stats.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.mental_stats.MentalStatsState


@Composable
fun MentalStatsScreen(state: MentalStatsState){
    Column(modifier = Modifier.fillMaxWidth()){
        Text(text = "mental stats screen")
        MentalControls(state = state)
    }

}