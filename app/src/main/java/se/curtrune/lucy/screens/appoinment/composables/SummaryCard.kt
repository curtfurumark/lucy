package se.curtrune.lucy.screens.appoinment.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import se.curtrune.lucy.classes.item.Item

@Composable
fun SummaryCard(item: Item, onSummaryChange: (String)->Unit){
    var summary by remember {
        mutableStateOf(item.comment)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {Text("summary")},
            minLines = 5,
            value = summary,
            onValueChange = {
                summary = it
                onSummaryChange(it)})
    }
}