package se.curtrune.lucy.screens.appoinment.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.classes.item.Item

@Composable
fun AddToTimeLineCard(timeLines: List<Item> = listOf()){
    var showDialog by remember {
        mutableStateOf(false)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        Text(text = "add to time line",
            modifier = Modifier.clickable(
                onClick = {
                    showDialog = true
                }
            )
        )
    }
    if( showDialog){
        AddToTimeLineDialog(
            items = timeLines,
            onAddToTimeLine = {
                showDialog = false
            }
            ,onDismiss = {
                showDialog = false
            }
        )
    }
}

@Composable
fun AddToTimeLineDialog(
    items: List<Item> = listOf(),
    onDismiss: ()->Unit,
    onAddToTimeLine: (Item)->Unit){
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(text = "add to time line")
            items.forEach {
                Text(text = it.heading)
            }
            Text(text = "create new time line")
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    onDismiss()
                }) {
                    Text(text = "dismiss")
                }
                Button(onClick = {
                    onAddToTimeLine(items.first())
                }) {
                    Text(text = "add")
                }
            }
        }
    }

}

@Composable
@Preview
fun PreviewAddToTimeLineCard(){
    AddToTimeLineCard()
}