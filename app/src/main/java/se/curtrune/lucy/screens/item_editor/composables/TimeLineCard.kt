package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp

@Composable
fun TimeLineCard(isTimeLineItem: Boolean, onIsTimeLineItemChange: (Boolean) -> Unit){
    var isTimeLine by remember { mutableStateOf(isTimeLineItem) }
    Card(modifier = Modifier.fillMaxWidth()){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "is timeline",
                modifier = Modifier.padding(start = 8.dp))
            Checkbox(
                checked = isTimeLine,
                onCheckedChange = {
                    isTimeLine = it
                    onIsTimeLineItemChange(isTimeLine)
                })
        }
    }
}
