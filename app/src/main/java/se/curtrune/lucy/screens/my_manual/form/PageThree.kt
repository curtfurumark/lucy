package se.curtrune.lucy.screens.my_manual.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.screens.my_manual.MyManualGuideEvent
import se.curtrune.lucy.screens.my_manual.MyManualGuideState

@Composable
fun PageThree(state: MyManualGuideState, onEvent: (MyManualGuideEvent)->Unit){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Text(text = "add")
            }
        }

    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "page three, ta hänsyn till")
            TriggerCard()
            Spacer(modifier = Modifier.weight(1f))
            Row() {
                Button(onClick = {
                    onEvent(MyManualGuideEvent.OnBackPressed)
                }){
                    Text(text = "back")
                }
                Button(onClick = {
                    onEvent(MyManualGuideEvent.OnPageThreeCompleted)
                }) {
                    Text(text = "next")
                }
            }
        }
    }
}
@Composable
fun TriggerCard(){
    var trigger by remember {
        mutableStateOf("")
    }
    Card(modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value =trigger,
            onValueChange = {trigger = it},
            placeholder = { Text(text = "ta hänsyn till...")},
        )
    }
}

@Composable
@Preview
fun PreviewPageThree(){
    PageThree(state = MyManualGuideState(), onEvent = {})

}