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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.screens.settings.UserEvent
import se.curtrune.lucy.screens.settings.UserState

@Composable
fun DarkModeSetting(state: UserState, onEvent: (UserEvent) -> Unit){
    var isDarkMode by remember {
        mutableStateOf(state.isDarkMode)
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
            Text(text = stringResource(R.string.dark_mode))
            Spacer(Modifier.weight(1f))
            Switch(checked = isDarkMode, onCheckedChange = {
                isDarkMode = !isDarkMode
                onEvent(UserEvent.DarkMode(isDarkMode))
            })
        }
    }
}