package se.curtrune.lucy.screens.dev.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.ItemDuration
import se.curtrune.lucy.composables.ItemSettingDuration
import se.curtrune.lucy.util.gson.MyGson

@Composable
fun DurationTest(){
    Column(modifier = Modifier.fillMaxWidth()) {
        var durationType by remember{
            mutableStateOf(ItemDuration.Type.SECONDS)
        }
        var itemDuration by remember {
            mutableStateOf(ItemDuration())
        }
        var hours by remember {
            mutableStateOf("0")
        }
        var minutes by remember {
            mutableStateOf("0")
        }
        var seconds by remember {
            mutableStateOf("0")
        }
        var itemDurationJson by remember {
            mutableStateOf("")
        }
        fun durationToJson(){
            itemDurationJson = MyGson.getMyGson().toJson(itemDuration)
        }
        Row(modifier = Modifier.fillMaxWidth()){
            Text(text = itemDurationJson)
        }
        Row(modifier = Modifier.fillMaxWidth()){
            Button(onClick = {
                durationToJson()
            }){
                Text(text = "to json")
            }
        }
        ItemSettingDuration(item = Item("i am item"),onEvent = {
            itemDuration = it
        })
        AnimatedVisibility(visible = durationType.equals(ItemDuration.Type.SECONDS)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                //Text(text = "hhh:mmm:ss")
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = hours,
                    label = {Text(text = "hour")},
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        hours = it
                        itemDuration.setHours(hours.toInt())
                    }
                )
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = minutes,
                    label = {Text(text = "minutes")},
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        minutes= it
                        if( minutes.isNotBlank()) {
                            itemDuration.setMinutes(minutes.toInt())
                        }
                    }
                )
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = seconds,
                    label = {Text(text = "seconds")},
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        seconds = it
                        itemDuration.setSeconds(seconds.toInt())
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewItemDuration(){
    LucyTheme {
        DurationTest()
    }
}