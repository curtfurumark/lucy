package se.curtrune.lucy.screens.settings.screens.panic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import se.curtrune.lucy.classes.PanicOption

@Composable
fun Five4321Card(state: PanicSettingState, onChecked: (Boolean)-> Unit){
    var checked by remember {
        mutableStateOf(state.panicOption == PanicOption.FIVE4321)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Text(text = "54321")
            Checkbox(checked = checked, onCheckedChange = {})

        }
    }
}
@Composable
@PreviewLightDark
fun Preview4321Card(){
    Five4321Card(state = PanicSettingState(), onChecked = {})
}