package se.curtrune.lucy.screens.settings.screens.panic

import android.R.attr.checked
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun UrlCard(
        state: PanicSettingState,
        onChecked: (Boolean)-> Unit,
        onUrlChange: (String)-> Unit){
    Card(modifier = Modifier.fillMaxWidth()){
        var url by remember {
            mutableStateOf("")
        }
        var checked by remember {
            mutableStateOf(false)
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        , verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                modifier = Modifier.weight(0.75f),
                value = url,
                onValueChange = { url = it },
                label = { Text("url") }
            )
            Checkbox(checked = checked, onCheckedChange = {
                checked = it
                onChecked(it)
            })
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewUrlCard(){
    UrlCard(state = PanicSettingState(), onChecked = {}, onUrlChange = {})

}


