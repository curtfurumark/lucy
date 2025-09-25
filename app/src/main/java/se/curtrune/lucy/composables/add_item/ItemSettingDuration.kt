package se.curtrune.lucy.composables.add_item

import android.icu.util.UniversalTimeScale.toLong
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toString
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.classes.ItemDuration
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.medicine.composable.DropdownItem

@Composable
fun ItemSettingDuration(item: Item, onEvent: (ItemDuration)->Unit){
    var showDropdown by remember {
        mutableStateOf(false)
    }
    var itemDuration by remember {
        mutableStateOf(ItemDuration())
    }
    var itemDurationType by remember {
        mutableStateOf(item.itemDuration?.type?: ItemDuration.Type.SECONDS)
    }
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                showDropdown = !showDropdown
            }){
        Column(modifier = Modifier.fillMaxWidth()){
            Text(text = stringResource(R.string.duration))
            Text(text = itemDurationType.name)
            DropdownMenu(expanded = showDropdown, onDismissRequest = {
                showDropdown = false
            }) {
                ItemDuration.Type.entries.forEach { durationType->
                    DropdownItem(action = durationType.name) {
                        itemDuration.type = durationType
                        itemDurationType = durationType
                        showDropdown = false
                        onEvent(itemDuration)
                    }
                }
            }
        }

    }
}

@Composable
fun HoursMinuteSeconds(duration: Long, onDurationChange: (Long)->Unit){
    var totalDuration by remember {
        mutableStateOf(duration)
    }
    var hours by remember {
        mutableLongStateOf(duration / 3600)
    }
    var minutes by remember {
        mutableStateOf((duration % 3600) / 60)
    }
    var seconds by remember {
        //mutableLongStateOf(duration % 60)
        mutableStateOf((duration % 60).toString())
    }
    fun updateDuration(){
        totalDuration = hours * 3600 + minutes * 60 + seconds.toLong()
        onDurationChange(totalDuration)
    }
    Row(modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = hours.toString(),
            onValueChange = {
                hours = it.toLong()
                updateDuration()
            },
            label = {
                Text(text = "hours")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = minutes.toString(),
            onValueChange = {
                println("on value change $it")
                if( it.isNotBlank()){
                    minutes = it.toLong()
                    updateDuration()
                }else{
                    minutes = 0
                    //updateDuration()
                }
                updateDuration()
            },
            label = {
                Text(text = "minutes")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = seconds.toString(),
            onValueChange = {
                if( it.isNotBlank()){
                    seconds = it
                    updateDuration()
                }else{
                    seconds = ""
                    println("on seconds is blank")
                    //updateDuration()
                }
            },
            label = {
                Text(text = "seconds")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}


@Composable
@PreviewLightDark
fun PreviewItemSettingDuration(){
    LucyTheme {
        //ItemSettingDuration(item = Item("hello"), onEvent = {})
        HoursMinuteSeconds(duration = 3665, onDurationChange = {})
    }
}