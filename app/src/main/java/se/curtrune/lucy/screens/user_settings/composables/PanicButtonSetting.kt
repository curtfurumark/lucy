package se.curtrune.lucy.screens.user_settings.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.user_settings.PanicOption
import se.curtrune.lucy.screens.user_settings.UserEvent
import se.curtrune.lucy.screens.user_settings.UserState

@Composable
fun PanicButton(state: UserState, onEvent: (UserEvent)->Unit){
    var showOptions by remember {
        mutableStateOf(false)
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    showOptions = !showOptions
                }){
            Text(
                text = stringResource( R.string.panic_action),
                fontSize = MaterialTheme.typography.titleMedium.fontSize)
            Spacer(Modifier.weight(1f))
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "choose panic action")
        }
        AnimatedVisibility(visible = showOptions) {
            Column(modifier = Modifier.fillMaxWidth()) {
                PanicOption.entries.forEach {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (it == state.panicOption), onClick = {
                            onEvent(UserEvent.SetPanicOption(it))
                        })
                        Text(text = it.name)
                    }
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewPanicButtonSetting() {
    LucyTheme {
        PanicButton(state = UserState(), onEvent = {})
    }
}