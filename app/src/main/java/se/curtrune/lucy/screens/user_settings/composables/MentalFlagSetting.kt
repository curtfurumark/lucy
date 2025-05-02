package se.curtrune.lucy.screens.user_settings.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.composables.MentalSlider
import se.curtrune.lucy.screens.user_settings.MentalFlag
import se.curtrune.lucy.screens.user_settings.UserEvent
import se.curtrune.lucy.screens.user_settings.UserState

@Composable
fun MentalFlagSetting(state: UserState, onEvent: (UserEvent) -> Unit){
    var showMentalFlags by remember { mutableStateOf(false) }
    val mentalFlag by remember {
        mutableStateOf(state.mentalFlag)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        Row ( modifier = Modifier.fillMaxWidth()
            .padding(8.dp)){
            Text(
                modifier = Modifier.clickable {
                    showMentalFlags = !showMentalFlags
                },
                text = "Mental flags",
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "expand")
        }
        AnimatedVisibility(visible = showMentalFlags) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 24.dp)
                    .clickable {
                        onEvent(UserEvent.UpdateMentalFlag(mentalFlag))
                    }){
                MentalSlider(mentalFlag.anxiety, label = "anxiety", onLevelChanged = {
                    mentalFlag.anxiety = it
                    onEvent(UserEvent.UpdateMentalFlag(mentalFlag))
                })
                MentalSlider(mentalFlag.energy, label = "energy", onLevelChanged = {
                    mentalFlag.energy = it
                    onEvent(UserEvent.UpdateMentalFlag(mentalFlag))
                })
                MentalSlider(mentalFlag.stress, label = "stress", onLevelChanged = {
                    mentalFlag.stress = it
                    onEvent(UserEvent.UpdateMentalFlag(mentalFlag))
                })
                MentalSlider(mentalFlag.mood, label = "mood", onLevelChanged = {
                    mentalFlag.mood = it
                    onEvent(UserEvent.UpdateMentalFlag(mentalFlag))
                })
            }
        }
    }
}


@Composable
@PreviewLightDark
fun PreviewMentalFlag(){
    val state = UserState()
    state.mentalFlag = MentalFlag(
        anxiety = 1,
        energy = 2,
        stress = 3,
        mood = 4
    )
    LucyTheme {
        MentalFlagSetting(state = state, onEvent = {})
    }
}