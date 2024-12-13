package se.curtrune.lucy.composables

import android.widget.AutoCompleteTextView.OnDismissListener
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.dialogs.RepeatDialog

@Composable
fun RepeatDialog(onDismiss: ()->Unit,
                 onConfirm: ()->Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), modifier   = Modifier
            .fillMaxSize(0.9f)
            .background(
                Color.Blue
            )){
            Column() {
                Text(text = "hello ", fontSize = 24.sp)
                 Button(onClick = { onDismiss }) {
                     Text(text = "cancel")
                 }
                Button(onClick = {onConfirm }) {
                    Text(text = "ok")
                }
            }
        }
    }
}
@Preview
@Composable
fun Preview(){
    RepeatDialog(onDismiss = {
        println(" repeatDialog dismissed")
    }) {
    }
}
