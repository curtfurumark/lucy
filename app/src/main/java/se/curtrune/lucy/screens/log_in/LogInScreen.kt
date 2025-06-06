package se.curtrune.lucy.screens.log_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.screens.log_in.ui.theme.LucyTheme


@Composable
fun LogInScreen(
        modifier: Modifier = Modifier,
        state: LogInState,
        onEvent:(LogInEvent)->Unit){

    var passWord by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val visualTransformation =
        if (showPassword) { VisualTransformation.None }
        else { PasswordVisualTransformation() }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = stringResource(R.string.log_in))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.password)) },
            value = passWord,
            onValueChange = { passWord = it },
            visualTransformation = visualTransformation,
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }){
                    Icon(imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = null)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            onEvent(LogInEvent.LogIn(passWord))
        }) {
            Text(text = stringResource(R.string.log_in))
        }
    }
}

@Preview(showBackground = true)
@Composable
@PreviewLightDark
fun PreviewLogInScreen(){
    LucyTheme {
        LogInScreen(modifier = Modifier.fillMaxSize(), state = LogInState()){}
    }
}