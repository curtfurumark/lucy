package se.curtrune.lucy.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.dialogs.PostponeDialog

enum class PostponeAmount{
    ONE_HOUR, TWO_HOURS, ONE_DAY, ONE_WEEK, ONE_MONTH
}
@Composable
fun PostponeDialog(onDismiss: ()->Unit, onConfirm: (PostponeAmount)->Unit){
    var postponeAmount by remember{
        mutableStateOf(PostponeAmount.ONE_HOUR)
    }
    Dialog(onDismissRequest = onDismiss){
        Surface(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "postpone heading")
                Row(horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = postponeAmount == PostponeAmount.ONE_HOUR,
                        onCheckedChange = {
                            postponeAmount = PostponeAmount.ONE_HOUR
                        })
                    Text(text = "one hour")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = postponeAmount == PostponeAmount.ONE_DAY, onCheckedChange = {
                        postponeAmount = PostponeAmount.ONE_DAY
                    })
                    Text(text = "one day")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = postponeAmount == PostponeAmount.ONE_WEEK,
                        onCheckedChange = {
                            postponeAmount = PostponeAmount.ONE_WEEK
                        })
                    Text(text = "one week")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = postponeAmount == PostponeAmount.ONE_MONTH,
                        onCheckedChange = {
                            postponeAmount = PostponeAmount.ONE_MONTH
                        })
                    Text(text = "one month")
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = onDismiss) {
                        Text(text = "dismiss")
                    }
                    Button(onClick = {
                        onConfirm(postponeAmount)
                    }) {
                        Text(text = "postpone")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDialog(){
    LucyTheme {
        PostponeDialog(onDismiss = {}, onConfirm = {})
    }
}