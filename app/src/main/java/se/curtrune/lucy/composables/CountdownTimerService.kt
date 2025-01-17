package se.curtrune.lucy.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.services.TimeServiceConstants

@Composable
fun CountDownTimerService(duration: Long, onCommand: (String)->Unit){
    var buttonText by remember{
        mutableStateOf("start")
    }
    var isRunning by remember {
        mutableStateOf(false)
    }
    var isCounting by remember {
        mutableStateOf(false)
    }
    Card(modifier =  Modifier.fillMaxWidth()){
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "countdown service", fontSize = 24.sp)
            TimeText(duration, fontSize = 24.sp)
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                //button start or resume
                Button(onClick = {
                    if( !isRunning && !isCounting){
                        onCommand(TimeServiceConstants.ACTION_START_COUNTDOWN_TIMER)
                        buttonText = "pause"
                        isRunning  = true
                        isCounting = true
                    }
                    else if( isRunning && isCounting){
                        buttonText = "resume"
                        onCommand(TimeServiceConstants.ACTION_PAUSE_COUNTDOWN_TIMER)
                    }
                    //onCommand(if( isRunning) "pause" el "start")
                    //isRunning = !isRunning
                    //buttonText = if(isRunning) "pause" else "start"
                }) { //start, resume
                    Text(text = buttonText)
                }
                Button(onClick = {
                    onCommand(TimeServiceConstants.ACTION_PAUSE_COUNTDOWN_TIMER)
                }) {
                    Text(text = "cancel")
                }
            }
        }
    }
}