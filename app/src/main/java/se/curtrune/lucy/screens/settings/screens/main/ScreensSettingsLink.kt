package se.curtrune.lucy.screens.settings.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.navigation.Route

@Composable
fun ScreensSettingsLink(onEvent: (UserEvent)->Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onEvent(UserEvent.Navigate(Route.ScreensScreenNavKey))
                }
            )
        )
    {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "screens")
            Spacer(Modifier.weight(1f))
            Text(text = ">")
        }
    }
}