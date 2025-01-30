package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.StateFlow
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.composables.MentalMeter
import se.curtrune.lucy.screens.dev.DevState

@Composable
fun MentalMeterTest(state: DevState){
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        , horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "MENTAL METER, PLEASE SWIPE")
            //state.mental?.let { MentalMeter(mental = it) }
        }
    }

}