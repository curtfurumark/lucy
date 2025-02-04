package se.curtrune.lucy.screens.main.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R

@Composable
fun ChoosePanicActionDialog(onDismiss: ()->Unit){

    Dialog(onDismissRequest = {}) {
        Surface(modifier =  Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()){
                Text(text = "phone home")
            }
            Row() {
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
    ChoosePanicActionDialog(onDismiss = {})
}