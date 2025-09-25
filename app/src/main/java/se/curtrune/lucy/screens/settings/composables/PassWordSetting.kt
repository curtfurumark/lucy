package se.curtrune.lucy.screens.settings.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.settings.UserEvent
import se.curtrune.lucy.screens.settings.UserState


@Composable
fun PassWordSetting( state: UserState, onEvent: (UserEvent) -> Unit){
    var usePassword by remember {
        mutableStateOf(state.usePassword)
    }
    var passWord by remember {
        mutableStateOf("")
    }
    var showPassword by remember {
        mutableStateOf(false)
    }
    val visualTransformation =
        if (showPassword) { VisualTransformation.None }
        else { PasswordVisualTransformation() }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.use_password))
                Spacer(modifier = Modifier.weight(1f))
                Checkbox(checked = state.usePassword, onCheckedChange = {
                    println("check")
                    usePassword = !usePassword
                    onEvent(UserEvent.UsePassword(usePassword))
                })
            }
            if(usePassword){
                Column() {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.password)) },
                        value = state.password,
                        onValueChange = { passWord = it },
                        visualTransformation = visualTransformation
                    )
                    Row(modifier = Modifier.fillMaxWidth()
                    , verticalAlignment = Alignment.CenterVertically) {
                        TriStateCheckbox(
                            state = if (showPassword) ToggleableState.On else ToggleableState.Off,
                            onClick = { showPassword = !showPassword }
                        )
                        Text(
                            text = stringResource(R.string.show_password),
                        )
                    }
                }
                Button(onClick = {
                    onEvent(UserEvent.SetPassword(passWord))
                }){
                    Text(text = "set password")
                }
            }
        }
    }
}

@Composable
@Preview
@PreviewLightDark
fun PreviewPasswordView(){
    LucyTheme {
        val state = UserState(usePassword = true)
        PassWordSetting(state = state,{ })
    }

}