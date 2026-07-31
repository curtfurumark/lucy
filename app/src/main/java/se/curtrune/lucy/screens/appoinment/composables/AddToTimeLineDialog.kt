package se.curtrune.lucy.screens.appoinment.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog

@Composable
fun AddToTimeLineDialog(onDismiss: ()->Unit){
    Dialog(onDismissRequest = onDismiss){
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "add to time line")
            Row(modifier = Modifier.fillMaxWidth()){
                Button(onClick = {onDismiss()}){
                    Text(text = "cancel")
                }
            }
        }
    }
}