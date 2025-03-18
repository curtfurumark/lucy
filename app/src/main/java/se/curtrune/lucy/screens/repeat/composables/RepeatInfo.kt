package se.curtrune.lucy.screens.repeat.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.item.Repeat

@Composable
fun RepeatInfo(repeat: Repeat){
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                println("on repeat click id ${repeat.id}")
            }) {
            Text(text = "id: ${repeat.id}", color = MaterialTheme.colorScheme.onSecondaryContainer)
            Text(
                text = "template id: ${repeat.templateID}",
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "first date: ${repeat.firstDate.toString()}",
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "last date ${repeat.lastDate.toString()}",
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "is infinity ${repeat.isInfinite}",
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "updated ${repeat.lastDate}",
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "${repeat.weekDays}"
            )
        }
    }
}