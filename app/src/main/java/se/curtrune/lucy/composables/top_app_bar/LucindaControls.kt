package se.curtrune.lucy.composables.top_app_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.composables.ColorCircle
import se.curtrune.lucy.composables.MentalMeter
import se.curtrune.lucy.screens.main.MainEvent
import se.curtrune.lucy.screens.main.MainState

@Composable
fun LucindaControls(state: MainState, onEvent: (TopAppBarEvent)->Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorCircle(color = Color.Green, onClick = {
                    onEvent(TopAppBarEvent.OnBoost)
                })
                MentalMeter(mental = state.mental)
                ColorCircle(color = Color.Red, onClick = {
                    onEvent(TopAppBarEvent.OnPanic)
                })
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun Preview(){
    LucindaControls(state = MainState(), onEvent = {})
}
