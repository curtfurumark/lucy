package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import se.curtrune.lucy.classes.item.Item

@Composable
fun DurationTimer(item: Item, onDurationChanged: (Item)->Unit){
    var duration by remember {
        mutableLongStateOf(0)
    }
    Card(
        modifier = Modifier.fillMaxWidth()){
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "00:00:00")
            Button(onClick = {}) {
                Text(text = "start")
                println("on start clicked")
                //onDurationChanged(item)
            }
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewDurationTimer(){
    DurationTimer(item = Item()) {
        println("on duration changed")
    }
}