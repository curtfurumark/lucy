package se.curtrune.lucy.screens.mental_stats.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.mental_stats.MentalDate

@Composable
fun MentalDateCard(modifier: Modifier = Modifier, mentalDate: MentalDate){
    LaunchedEffect(Unit) {
        mentalDate.items.forEach {
            if( it.mood != 0) {
                println("item: ${it.heading}")
                println(it.mental)
                println()
            }
        }
    }
    Card(modifier = modifier.fillMaxWidth()){
        Text(
            text = mentalDate.date.toString(),
            fontSize = MaterialTheme.typography.headlineMedium.fontSize)
        Text(text = "energy ${mentalDate.energy}")
        Text(text = "mood ${mentalDate.mood}")
        Text(text = "anxiety ${mentalDate.anxiety}")
        Text(text = "stress ${mentalDate.stress}")
    }
}