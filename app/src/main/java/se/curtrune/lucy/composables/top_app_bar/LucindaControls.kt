package se.curtrune.lucy.composables.top_app_bar

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import se.curtrune.lucy.composables.ColorCircle
import se.curtrune.lucy.composables.MentalMeter
import se.curtrune.lucy.screens.main.MainEvent
import se.curtrune.lucy.screens.main.MainState
import se.curtrune.lucy.screens.main.TopAppBarState

@Composable
fun LucindaControls(state: TopAppBarState, onEvent: (TopAppBarEvent)->Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorCircle(color = Color.Green.copy(alpha = 0.4f), onClick = {
                    onEvent(TopAppBarEvent.OnBoost)
                })
                Text(
                    text = state.title,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                //MentalMeter(mental = state.mental)
                ColorCircle(color = Color.Red.copy(alpha = 0.4f), onClick = {
                    onEvent(TopAppBarEvent.OnPanic)
                })
            }
        }
    }
}
@Preview(showBackground = true)
@PreviewLightDark
@Composable
fun Preview(){
    LucindaControls(state = TopAppBarState(), onEvent = {})
}
