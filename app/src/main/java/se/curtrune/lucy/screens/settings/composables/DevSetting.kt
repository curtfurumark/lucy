package se.curtrune.lucy.screens.settings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.screens.settings.UserEvent
import se.curtrune.lucy.screens.settings.UserState

@Composable
fun DevSetting(state: UserState, onEvent: (UserEvent) -> Unit){

    var isDevMode by remember {
        mutableStateOf(state.isDevMode)
    }
    Card(modifier = Modifier
        .fillMaxWidth(), shape = RoundedCornerShape(8.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically)
        {
            Text(text = "dev mode")
            Spacer(Modifier.weight(1f))
            Switch(checked = isDevMode, onCheckedChange = {
                isDevMode = !isDevMode
                onEvent(UserEvent.DevMode(isDevMode))
            })
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewDevSetting(){
    LucyTheme {
        DevSetting(state = UserState(), onEvent = {})
    }

}