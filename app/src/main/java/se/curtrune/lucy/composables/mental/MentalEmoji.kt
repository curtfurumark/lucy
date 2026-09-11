package se.curtrune.lucy.composables.mental

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.item.Item

@Composable
fun MentalEmoji(item: Item, onChanged: (Int)->Unit){
    var currentEnergy by remember {
        mutableStateOf(item.energy)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(text = "current ${currentEnergy}")
            }
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "\uD83D\uDE2B",
                    modifier = Modifier.clickable(onClick = {
                        currentEnergy = -2
                        onChanged(-2) })
                )
                Text(
                    text = "\uD83D\uDE41",
                    modifier = Modifier.clickable(
                        onClick = {
                            currentEnergy = -1
                            onChanged(-1) })
                )
                Text(
                    text = "\uD83D\uDE10",
                    modifier = Modifier.clickable(
                        onClick = {
                            currentEnergy = 0
                            onChanged(0) })
                )
                Text(
                    text = "\uD83D\uDE42",
                    modifier = Modifier.clickable(
                        onClick = {
                            currentEnergy = 1
                            onChanged(1) })
                )

                Text(
                    text = "\uD83D\uDE01",
                    modifier = Modifier.clickable(
                        onClick = {
                            currentEnergy = 2
                            onChanged(2) })
                )
            }
        }
    }
}


@Composable
@PreviewLightDark
fun PreviewMentalEmoji(){
    val item = Item("mood")
    MentalEmoji(item = item, onChanged = {
        println("onChanged")
    })
}