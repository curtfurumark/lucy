package se.curtrune.lucy.screens.settings.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.screens.navigation.Route
import se.curtrune.lucy.screens.settings.screens.main.UserEvent

@Composable
fun PanicLinkCard(onEvent: (UserEvent)->Unit){
    Card(
        modifier = Modifier.fillMaxWidth()
            .height(32.dp)
            .clickable(
                onClick = {
                    onEvent(UserEvent.Navigate(Route.PanicSettingsScreenNavKey))
                }
            )
        ){
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Text(text ="panic settings")
            Text(text = ">")

        }
    }
}

@Composable
@PreviewLightDark
fun PreviewPanicLinkCard(){
    PanicLinkCard {  }
}