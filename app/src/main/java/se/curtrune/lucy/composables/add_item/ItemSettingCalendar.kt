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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.classes.item.Item

@Composable
fun ItemSettingCalendar(modifier: Modifier = Modifier, item: Item, onEvent: (Boolean) -> Unit){
    var isCalendarItem by remember {
        mutableStateOf(item.isCalenderItem)
    }
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier.padding(start = 8.dp),
                text = stringResource(R.string.is_calender))
            Checkbox(checked = isCalendarItem, onCheckedChange = {
                isCalendarItem = !isCalendarItem
                onEvent(isCalendarItem)
            })
        }
    }
}