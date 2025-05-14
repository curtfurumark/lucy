package se.curtrune.lucy.screens.dev.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.dev.DevEvent
import se.curtrune.lucy.screens.dev.DevState

@Composable
fun DevScreen(modifier: Modifier = Modifier, state: DevState = DevState(), onEvent: (DevEvent) -> Unit = {}){
    HolidaysTest(state = state, onEvent = onEvent)
}


@Composable
@PreviewLightDark
fun PreviewDevScreen(){
    LucyTheme {
        DevScreen()
    }
}