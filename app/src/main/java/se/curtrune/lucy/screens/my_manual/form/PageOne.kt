package se.curtrune.lucy.screens.my_manual.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
fun PageOne(state: MyManualGuideState, onEvent: (MyManualGuideEvent)->Unit) {
    val dataOne by remember { mutableStateOf(DataOne())}
    var name by remember { mutableStateOf(state.name) }
    var dob by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    //var telephone by remember { mutableStateOf("") }
    //var iceNumber by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()){
        Text(text = "page one, personal data")
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                dataOne.name = it
                onEvent(MyManualGuideEvent.OnNameChanged(name))
            },
            label = { Text(text = "name")},
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = dob,
            onValueChange = {dob = it},
            label = { Text(text = "date of birth")},
            modifier = Modifier.fillMaxWidth()
            )
        OutlinedTextField(
            value = height,
            onValueChange = {height = it},
            label = { Text(text = "height")},
            modifier = Modifier.fillMaxWidth()
            )
        OutlinedTextField(
            value = weight,
            onValueChange = {weight = it},
            label = { Text(text = "weight")},
            modifier = Modifier.fillMaxWidth()
            )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onEvent(MyManualGuideEvent.OnPageOneCompleted(dataOne)) }) {
            Text(text = "next")
        }
    }
}
data class DataOne(
    var name: String = "",
    var dob: Int = 0,
    var  height: Int = 0,
    var  weight: Int = 0,
    var  telephone: String = ""
)


@Composable
@Preview
fun PreviewPageOne(){
    PageOne(state = MyManualGuideState(), onEvent = {})
}