package se.curtrune.lucy.composables.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R

@Composable
fun TagsDialog(onDismiss: ()->Unit, onConfirm: (String)->Unit){
    var tags by remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest =onDismiss ) {
        Card(modifier = Modifier.fillMaxWidth()){
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.tags), fontSize = 24.sp)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = tags, onValueChange = {
                    tags = it
                },
                    label = { Text(text = stringResource(R.string.tags)) })
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = onDismiss) {
                        Text(text = stringResource(R.string.dismiss))
                    }
                    Button(onClick = { onConfirm(tags) }) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }

}