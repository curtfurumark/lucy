package se.curtrune.lucy.screens.settings.screens.panic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp

@Composable
fun BreatheSettingCard(onBreatheChecked: (Boolean)-> Unit){
    var checked by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable{}
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "breathe", modifier = Modifier.padding(start = 8.dp))
            Checkbox(checked = checked, onCheckedChange = {
                checked = it }
            )
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewBreatheSettingCard(){
    BreatheSettingCard {  }

}