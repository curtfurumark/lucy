package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.dialogs.TimePickerDialog
import se.curtrune.lucy.util.DateTImeConverter
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSettingTime(item: Item, onTimeChanged: (LocalTime) -> Unit){
    var targetTime by remember {
        mutableStateOf(item.targetTime)
    }
    var showTimeDialog by remember {
        mutableStateOf(false)
    }
    var hasTime = item.targetTimeSecondOfDay > 0
    Card(modifier = Modifier.fillMaxWidth()){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable{
                    showTimeDialog = true
                }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if( hasTime){
                Text(
                    text = DateTImeConverter.format(targetTime),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "chose time",
                    modifier = Modifier.clickable {
                        hasTime = false
                        targetTime = LocalTime.ofSecondOfDay(0)
                        onTimeChanged(targetTime)
                    }
                )

            }else {
                Text(
                    text = stringResource(R.string.time),
                    modifier = Modifier.clickable {
                        showTimeDialog = true
                    }
                )
            }
        }
    }
    if( showTimeDialog){
        TimePickerDialog(
            onDismiss = {showTimeDialog = false},
            onConfirm = {timePickerState ->
                targetTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                onTimeChanged(targetTime)
                showTimeDialog = false
            }
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewItemSettingTime(){
    LucyTheme {
        val item = Item()
        ItemSettingTime(item = item, onTimeChanged = {})
    }
}