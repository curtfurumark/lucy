package se.curtrune.lucy.screens.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.composables.ColorCircle
import se.curtrune.lucy.composables.MentalMeter
import se.curtrune.lucy.screens.dev.DevState
import se.curtrune.lucy.screens.main.MainEvent
import se.curtrune.lucy.screens.main.MainState

@Composable
fun LucindaControls(state: MainState, onEvent: (MainEvent)->Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            ColorCircle(color = Color.Green, onClick ={
                onEvent(MainEvent.ShowBoost(true))
            } )
            MentalMeter(mental =  state.mental)
            ColorCircle(color = Color.Red, onClick = {
                onEvent(MainEvent.ShowPanic(true))
            })
        }
    }
}
@Preview(showBackground = true)
@Composable
fun Preview(){
    LucindaControls(state = MainState(), onEvent = {})
}
