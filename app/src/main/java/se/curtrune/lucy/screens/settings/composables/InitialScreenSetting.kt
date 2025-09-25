package se.curtrune.lucy.screens.settings.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.app.InitialScreen
import se.curtrune.lucy.screens.settings.UserEvent
import se.curtrune.lucy.screens.settings.UserState

@Composable
fun InitialScreenSetting(state: UserState, onEvent: (UserEvent)->Unit){
    var expanded by remember {
        mutableStateOf(false)
    }
    var initialScreen by remember {
        mutableStateOf(state.initialScreen)
    }

    Card(modifier = Modifier.fillMaxWidth()){
        Text(
            text = "initial screen: ${initialScreen.toString()}",
            modifier = Modifier.clickable{
                expanded = true
            })
        DropdownMenu(expanded = expanded, onDismissRequest = {}) {
            InitialScreen.entries.forEach {
                Text(
                    text = it.toString(),
                    modifier = Modifier.clickable {
                        expanded = false
                        initialScreen = it
                        onEvent(UserEvent.SetInitialScreen(it))
                    }
                )

            }
        }

    }

}


@Composable
@PreviewLightDark
fun PreviewInitialScreenSetting(){
    LucyTheme {
        InitialScreenSetting(state = UserState(initialScreen = InitialScreen.CALENDER_DATE), onEvent = {})

    }
}