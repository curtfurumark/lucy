package se.curtrune.lucy.screens.main.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R
import se.curtrune.lucy.screens.affirmations.Affirmation

@Composable
fun AffirmationDialog(onDismiss: ()-> Unit, affirmation: Affirmation){
    Dialog(onDismissRequest = {onDismiss()}){
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = affirmation.affirmation)
            Button(onClick = {onDismiss()}) {
                Text(text = stringResource(R.string.ok))
            }
        }
    }
}