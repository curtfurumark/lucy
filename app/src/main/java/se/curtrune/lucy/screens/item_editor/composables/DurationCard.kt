package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.item.Item
import kotlin.time.Duration

@Composable
fun DurationCard(modifier: Modifier = Modifier, duration: Long, onDurationChanged: (Long) -> Unit) {
    var visible by remember {
        mutableStateOf(false)
    }
    var duration by remember {
        mutableLongStateOf(duration)
    }
    Card(modifier = modifier.clickable {
        visible = !visible
    }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween)

        {

            Text(
                modifier = modifier.padding(8.dp),
                text = "duration: ${duration}")
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }
        AnimatedVisibility(visible = visible) {
            HoursMinuteSeconds(duration = duration, onDurationChange = {
                duration = it
                onDurationChanged(it)
            })

        }
    }
}

@Composable
fun HoursMinuteSeconds(duration: Long, onDurationChange: (Long) -> Unit) {
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

    fun updateDuration() {
        totalDuration = hours * 3600 + minutes * 60 + seconds.toLong()
        onDurationChange(totalDuration)
    }
    Row(modifier = Modifier.fillMaxWidth()) {
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
                if (it.isNotBlank()) {
                    minutes = it.toLong()
                    updateDuration()
                } else {
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
                if (it.isNotBlank()) {
                    seconds = it
                    updateDuration()
                } else {
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