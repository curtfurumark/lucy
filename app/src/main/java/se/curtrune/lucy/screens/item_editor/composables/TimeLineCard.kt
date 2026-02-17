package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun TimeLineCard(isTimeLineItem: Boolean, onIsTimeLineItemChange: (Boolean) -> Unit){
    var isTimeLineItem by remember { mutableStateOf(false) }
    Card(modifier = Modifier.fillMaxWidth()){
        Row(
            modifier = Modifier.fillMaxWidth()){
            Text(text = "is timeline")
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(checked = isTimeLineItem, onCheckedChange = onIsTimeLineItemChange)
        }
    }
}
