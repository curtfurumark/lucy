package se.curtrune.lucy.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
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
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.classes.Item

enum class PostponeAmount{
    ONE_HOUR, TWO_HOURS, ONE_DAY, ONE_WEEK, ONE_MONTH
}
data class PostponeDetails(
    var amount: PostponeAmount = PostponeAmount.ONE_HOUR,
    var postPoneAll: Boolean = false,
    var item: Item? = null
)

@Composable
fun PostponeDialog(onDismiss: ()->Unit, onConfirm: (PostponeDetails)->Unit, item: Item){
    var postponeInfo by remember {
        mutableStateOf(PostponeDetails(item = item))
    }
    Dialog(onDismissRequest = onDismiss){
        Surface(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp)) {
                    Text(text = stringResource(R.string.postpone_dialog_heading, item.heading))
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = postponeInfo.postPoneAll,
                            onCheckedChange = { checked ->
                                postponeInfo = postponeInfo.copy(postPoneAll = checked )
                            })
                        Text(text = "postpone all")
                    }
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = postponeInfo.amount == PostponeAmount.ONE_HOUR,
                            onClick = {
                                postponeInfo = postponeInfo.copy(amount = PostponeAmount.ONE_HOUR)
                            })
                        Text(text = stringResource(R.string.one_hour))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = postponeInfo.amount == PostponeAmount.ONE_DAY,
                            onClick = {
                                postponeInfo = postponeInfo.copy(amount = PostponeAmount.ONE_DAY)
                            })
                        Text(text = stringResource(R.string.one_day))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = postponeInfo.amount == PostponeAmount.ONE_WEEK,
                            onClick = {
                                postponeInfo = postponeInfo.copy(amount = PostponeAmount.ONE_WEEK)
                            })
                        Text(text = stringResource(R.string.one_day))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = postponeInfo.amount == PostponeAmount.ONE_MONTH,
                            onClick = {
                                postponeInfo = postponeInfo.copy(amount = PostponeAmount.ONE_MONTH)
                            })
                        Text(text = stringResource(R.string.one_month))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = onDismiss) {
                            Text(text = stringResource(R.string.dismiss))
                        }
                        Button(onClick = {
                            onConfirm(postponeInfo)
                        }) {
                            Text(text = stringResource(R.string.postpone))
                        }
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
        PostponeDialog(onDismiss = {}, onConfirm = {}, Item("i am an item"))
    }
}