package se.curtrune.lucy.composables.dialogs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.screens.week_calendar.WeekEvent


@Composable
fun AddAllWeekItemDialog(week: Week, dismiss: ()->Unit, confirm: (String)->Unit){
    var heading by remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = {

    }) {
        Card(modifier = Modifier.fillMaxWidth()){
            OutlinedTextField(
                value = heading,
                onValueChange = {
                    heading = it
                },
                label = {
                    Text(text = "heading")
                }

            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    println("add all week item")
                })
                {
                    Text(text = "dismiss")

                }
                Button(onClick = {
                    println("add all week item")
                })
                {
                    Text(text = "add item")

                }
            }
        }
    }
}