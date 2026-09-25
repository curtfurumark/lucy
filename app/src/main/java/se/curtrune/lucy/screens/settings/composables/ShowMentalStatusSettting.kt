package se.curtrune.lucy.screens.settings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.settings.UserEvent
import se.curtrune.lucy.screens.settings.UserState

@Composable
fun ShowMentalStatusSetting(state: UserState, onEvent: (UserEvent) -> Unit){
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "show mental status")
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(
                checked = state.showMentalStatus,
                onCheckedChange = {
                    onEvent(UserEvent.ShowMentalStatusChanged(it))
                }
            )
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