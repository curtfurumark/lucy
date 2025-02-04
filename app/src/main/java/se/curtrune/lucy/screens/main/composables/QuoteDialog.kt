package se.curtrune.lucy.screens.main.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R
import se.curtrune.lucy.screens.affirmations.Quote


@Composable
fun QuoteDialog(onDismiss: ()-> Unit, quote: Quote){
    Dialog(onDismissRequest = {onDismiss()}){
        Card(modifier =  Modifier.background(color = Color.DarkGray)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = quote.q)
                Text(text = quote.a)
                Button(onClick = { onDismiss() }) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        }
    }
}