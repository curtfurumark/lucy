package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.item.Repeat
import se.curtrune.lucy.composables.RepeatDialog

@Composable
fun ItemSettingRepeat(item: Item, onRepeatEvent: (Repeat) -> Unit){
    val hasRepeat = item.hasRepeat()
    var showRepeatDialog by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier.fillMaxWidth()
            .height(40.dp)
            .clickable {
                showRepeatDialog = true
            }) {
        Row(
            modifier = Modifier.padding(4.dp)
                , verticalAlignment = Alignment.CenterVertically) {
            if( hasRepeat){
                Text(text = item.repeat.toString())
            }else{
                Icon(imageVector = Icons.Default.Repeat, contentDescription = "repeat")
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = stringResource(R.string.repeat))
            }
        }
    }
    if( showRepeatDialog){
        RepeatDialog(onDismiss = {
            showRepeatDialog = false
        }, onConfirm = { repeat ->
            println("on confirm repeat")
            item.repeat = repeat
            onRepeatEvent(repeat)
            showRepeatDialog = false
        })
    }
}
@Composable
fun ItemSettingRepeat(repeat: Repeat?, onEvent: () -> Unit){
    Card(modifier = Modifier.fillMaxWidth()
        .border(Dp.Hairline, color = Color.LightGray)
        .clickable {
            onEvent()
        })
    {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth()
                .clickable {
                    onEvent()
                })
        {
            Text(text = stringResource(R.string.repeat))
            if (repeat != null) {
                Text(text = repeat.toString())
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewItemSettingRepeat(){
    LucyTheme {
        val item = Item("hello")
        val repeat = Repeat()
        repeat.qualifier = 1
        repeat.unit = Repeat.Unit.WEEK
        //item.repeat = repeat
        ItemSettingRepeat(item = item, onRepeatEvent = {

        })
    }

}