package se.curtrune.lucy.screens.my_manual.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.screens.my_manual.MyManualGuideEvent
import se.curtrune.lucy.screens.my_manual.MyManualGuideState

@Composable
fun PageSensories(state: MyManualGuideState, onEvent: (MyManualGuideEvent)->Unit){
    var soundChecked by remember { mutableStateOf(false) }
    var touchChecked by remember { mutableStateOf(false) }
    var lightChecked by remember { mutableStateOf(false) }
    Column(){
        Text(text = "page four, sensories")
        CheckableCard("sound", soundChecked){
            println("sound checked")
            soundChecked = !soundChecked
        }
        CheckableCard("touch",  touchChecked){
            println("vibration checked")
            touchChecked = !touchChecked
        }
        CheckableCard("light", lightChecked){
            println("light checked ")
            lightChecked = !lightChecked
        }
        Spacer(modifier = Modifier.weight(1f))
        Row() {
            Button(onClick = {
                onEvent(MyManualGuideEvent.OnBackPressed)
            }) {
                Text(text = "back")

            }
            /*Button(onClick = {
                onEvent(MyManualGuideEvent.OnPageFourCompleted)
            }) {
                Text(text = "next")
            }
           */
        }
    }
}



@Composable
fun CheckableCard(text: String, checked: Boolean, onCheckedChange: (Boolean)-> Unit){
    Card(
        modifier = Modifier.fillMaxWidth()){
        Row(
            modifier = Modifier.fillMaxWidth(),
            //horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Checkbox(checked = checked, onCheckedChange = onCheckedChange)
            Text(text = text)
        }
    }
}

@Composable
@Preview
fun PreviewPageFour(){
    PageSensories(state = MyManualGuideState(), onEvent = {})
}