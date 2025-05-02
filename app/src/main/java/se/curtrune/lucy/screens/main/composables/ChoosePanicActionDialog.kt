package se.curtrune.lucy.screens.main.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R
import se.curtrune.lucy.screens.main.MainEvent
import se.curtrune.lucy.screens.main.MainState

@Composable
fun ChoosePanicActionDialog(
    state: MainState = MainState(),
    onEvent: (MainEvent)->Unit,
    onDismiss: ()->Unit){
    var showDiary by remember {
        mutableStateOf(false)
    }
    var diaryText by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = {}) {
        Card(modifier =  Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)) {
/*            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = "phone home")
            }*/
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "add to diary")
                Checkbox(onCheckedChange = {
                    showDiary = it
                }, checked = showDiary)
            }
            AnimatedVisibility(visible = showDiary) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = diaryText, onValueChange = {
                            diaryText = it
                        }, label = {
                        Text(text = "diary")
                    })
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    onDismiss()
                }) {
                    Text(text = stringResource(R.string.dismiss))
                }
                Button(onClick = {}) {
                    Text(text = stringResource(R.string.confirm))
                }
            }

        }
    }
    }
}
@Composable
@Preview(showBackground = true)
fun PreviewPanicDialog(){
    ChoosePanicActionDialog(onDismiss = {}, onEvent = {})
}