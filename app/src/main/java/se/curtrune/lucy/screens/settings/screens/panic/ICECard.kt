package se.curtrune.lucy.screens.settings.screens.panic

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun ICECard(state: PanicSettingState, onEvent: (PanicSettingEvent)->Unit){
    var number by remember {
        mutableStateOf(1234556789)
    }
    var checked by remember {
        mutableStateOf(false)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        Row() {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "ice") },
                value = number.toString(),
                onValueChange = {})
            Checkbox(checked = checked, onCheckedChange = {
                checked = it
            })
        }
    }
}


@Composable
@PreviewLightDark
fun PreviewICECard(){
    val state = PanicSettingState()
    ICECard(state = state){

    }
}