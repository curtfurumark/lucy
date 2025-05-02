package se.curtrune.lucy.screens.user_settings.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.user_settings.UserEvent
import se.curtrune.lucy.screens.user_settings.UserState

@Composable
fun ShowMentalStatusSetting(state: UserState, onEvent: (UserEvent) -> Unit){
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = state.showMentalStatus, onCheckedChange = {
                onEvent(UserEvent.ShowMentalStatusChanged(it))
            })
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "show mental status")
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewShowMentalStatusSetting(){
    LucyTheme {
        ShowMentalStatusSetting(state = UserState(), onEvent = {})
    }

}