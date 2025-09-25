package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.DatePickerModal
import se.curtrune.lucy.util.DateTImeConverter
import java.time.LocalDate

@Composable
fun ItemSettingDate(date: LocalDate, onEvent: ()->Unit){
    println("ItemSettingDate(date: $date)")
    Card(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)
        .clickable { onEvent() }) {
        Row(modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text = stringResource(R.string.date)
            )
            if (date.toEpochDay() > 0) {
                Text(text = DateTImeConverter.format(date))
            }
        }
    }
}
@Composable
fun ItemSettingDate(item: Item, onDateChanged: (LocalDate)->Unit){
    println("ItemSettingDate(item: $item)")
    var showDateDialog by remember {
        mutableStateOf(false)
    }
    var targetDate by remember {
        mutableStateOf(item.targetDate)
    }
    var hasDate = item.targetDate.toEpochDay() > 0
    Card(modifier = Modifier.fillMaxWidth())
    {
        Row(modifier = Modifier.fillMaxWidth()
            .padding(8.dp)
            .clickable{
                showDateDialog = true
            },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically)
        {
            if (hasDate) {
                Text(
                    text = DateTImeConverter.format(targetDate),
                    modifier = Modifier.clickable {
                        println("date clicked")
                        showDateDialog = true
                    })
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete time",
                    modifier = Modifier.clickable {
                        println("delete time clicked")
                        targetDate = LocalDate.ofEpochDay(0)
                        item.targetDate =targetDate
                        hasDate = false
                        onDateChanged(item.targetDate)
                    })
            }else{
                Text(
                    text = stringResource(R.string.date),
                    modifier = Modifier.clickable {
                        showDateDialog = true
                    }
                )
            }
        }
    }
    if(showDateDialog){
        DatePickerModal( onDismiss = {
            showDateDialog = false
        }, onDateSelected = { date ->
            targetDate = date
            onDateChanged(date)
            showDateDialog = false
        })
    }
}

@Composable
@PreviewLightDark
fun PreviewItemSettingDate(){
    LucyTheme {
        val item = Item()
        item.targetDate = LocalDate.now()
        ItemSettingDate(item, onDateChanged = {})
    }
}