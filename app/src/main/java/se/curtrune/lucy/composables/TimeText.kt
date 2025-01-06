package se.curtrune.lucy.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.dialogs.DurationDialog
import java.time.Duration

@Composable
fun TimeText(seconds: Long, fontSize: TextUnit){
    var timerText = formatSeconds(seconds)
    Text(
        text = timerText,
        fontSize = fontSize
    )
}
fun formatSeconds(seconds: Long): String{
    val secondsPart = seconds % 60
    val minutesPart = seconds / 60
    val hourPart = seconds / 3600
    val timerText = "${hourPart.toString().padStart(2, '0')}:${minutesPart.toString().padStart(2, '0')}:${secondsPart.toString().padStart(2, '0')}"
    return timerText
}


@Composable
fun DurationDialog(initialDuration: Long, onConfirm: (Long)->Unit, onDismiss: ()->Unit){
    var duration by remember {
        mutableStateOf(initialDuration)
    }
    var newDuration by remember{
        mutableLongStateOf(0L)
    }
    var hours by remember {
        mutableStateOf("00")
    }
    var minutes by remember{
        mutableStateOf("00")
    }
    var seconds by remember {
        mutableStateOf("00")
    }
    Dialog(
        onDismissRequest = onDismiss){
        Card(modifier = Modifier.fillMaxWidth()
            .padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(text = "edit time", fontSize = 24.sp)
                TimeText(seconds = duration, 20.sp)
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedTextField(
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = hours, onValueChange = {
                            hours = it
                            newDuration += if(it.isNotEmpty()) it.toInt() * 3600 else 0
                        },
                        placeholder = {
                            Text(text = "hours", maxLines = 1)
                        }
                    )
                    OutlinedTextField(
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = minutes, onValueChange = {
                            minutes = it
                            newDuration += if(it.isNotEmpty()) 60 * it.toInt() else 0
                        },
                        placeholder = {
                            Text(text = "minutes", maxLines = 1)
                        }
                    )
                    OutlinedTextField(
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = seconds, onValueChange = {
                            seconds = it
                            newDuration += if (it.isNotEmpty()) it.toInt() else 0
                        },
                        placeholder = {
                            Text(text = "seconds", maxLines = 1)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onDismiss) {
                        Text(text = "dismiss")
                    }
                    Button(onClick = {
                        println("onConfirm, hours $hours, minutes $minutes, seconds $seconds")
                        onConfirm(newDuration)
                    }) {
                        Text(text = "ok")
                    }
                }
            }
        }
    }
}