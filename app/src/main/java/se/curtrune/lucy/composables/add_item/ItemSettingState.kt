package se.curtrune.lucy.composables.add_item

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
import se.curtrune.lucy.classes.item.Item
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R

@Composable
fun ItemSettingState(item: Item, onChange: (Boolean)->Unit){
    var isDone by remember {
        mutableStateOf(item.isDone)
    }
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "done",
                modifier = Modifier.padding(start= 8.dp))
            Checkbox(checked = isDone, onCheckedChange = {
                isDone = !isDone
                onChange(isDone)
            })
        }
    }

}