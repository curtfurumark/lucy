package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item

@Composable
fun ItemSettingAppointment(item: Item, onEvent: (Boolean) -> Unit){
    //println("ItemSettingAppointment()is appointment: ${item.isAppointment}")
    var isAppointment by remember {
        mutableStateOf(item.isAppointment)
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "appointment",
                modifier = Modifier.padding(4.dp))
            Checkbox(checked = isAppointment, onCheckedChange = {
                isAppointment = !isAppointment
                onEvent(isAppointment)
            })
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewItemSettingAppointment(){
    LucyTheme {
        val item = Item("doktorn")
        item.isAppointment = true
        ItemSettingAppointment(item = item, onEvent = {})
    }
}