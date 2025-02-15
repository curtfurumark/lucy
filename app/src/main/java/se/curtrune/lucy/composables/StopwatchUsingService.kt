package se.curtrune.lucy.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import se.curtrune.lucy.services.ServiceConstants.ACTION_CANCEL_STOPWATCH
import se.curtrune.lucy.services.ServiceConstants.ACTION_PAUSE_STOPWATCH
import se.curtrune.lucy.services.ServiceConstants.ACTION_START_STOPWATCH
import se.curtrune.lucy.services.TimerService


@Composable
fun StopWatchUsingService( onCommand: (String)->Unit) {
    var currentSeconds by remember {
        mutableLongStateOf(0L)
    }

    TimerService.currentDuration.observe(LocalLifecycleOwner.current) {
        currentSeconds = it
    }
    var buttonText by remember {
        mutableStateOf("start")
    }
    //is counting
    var isRunning by remember {
        mutableStateOf(false)
    }
    //started but not canceled
    var isPaused by remember {
        mutableStateOf(false)
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "timer using service", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            TimeText(seconds = currentSeconds, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    println("cancel stop watch");
                    isRunning = false
                    isPaused = false
                    buttonText = "start"
                    onCommand(ACTION_CANCEL_STOPWATCH)
                }) {
                    Text(text = "cancel")
                }
                Button(onClick = {
                    if (!isRunning || isPaused) {
                        println("start or resume")
                        isRunning = true
                        isPaused = false
                        buttonText = "pause"
                        onCommand(ACTION_START_STOPWATCH)

                    } else {
                        println("pause")
                        isPaused = true
                        isRunning =  false
                        buttonText = "resume"
                        onCommand(ACTION_PAUSE_STOPWATCH)
                    }
                }) {
                    Text(text = buttonText)
                }
            }
        }
    }
}