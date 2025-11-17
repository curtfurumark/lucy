package se.curtrune.lucy.screens.mental_stats.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.mental_stats.MentalDate

@Composable
fun MentalStatCard(mentalDate: MentalDate){
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(text = mentalDate.date.toString())
        Text(text = mentalDate.energy.toString())

    }

}