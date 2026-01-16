package se.curtrune.lucy.activities.kotlin.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.item.DurationEdit
import se.curtrune.lucy.composables.Field
import se.curtrune.lucy.composables.MentalSlider


@Composable
fun EditItemCard(item: Item, onItemEdit: (Item)->Unit, itemField: Field){
    Card(
        modifier =
        Modifier.fillMaxWidth()
            .padding(4.dp)
            .background(Color.DarkGray)){
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = item.targetTime.toString(), fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.heading, fontSize = 20.sp)
            }
            if(itemField == Field.ENERGY) {
                MentalSlider(item.energy, "energy", onLevelChanged = {
                    println("energy changed $it")
                    item.energy = it
                    onItemEdit(item)
                })
            }
            if( itemField == Field.ANXIETY) {
                MentalSlider(item.anxiety, "anxiety", onLevelChanged = {
                    println("anxiety changed $it")
                    item.anxiety = it
                    onItemEdit(item)
                })
            }
            if(itemField == Field.MOOD) {
                MentalSlider(item.mood, "mood", onLevelChanged = {
                    println("mood changed $it")
                    item.mood = it
                    onItemEdit(item)
                })
            }
            if(itemField == Field.STRESS) {
                MentalSlider(item.stress, "stress", onLevelChanged = {
                    println("stress changed $it")
                    item.stress = it
                    onItemEdit(item)
                })
            }
            if(itemField == Field.DURATION) {
                DurationEdit(item.duration, onDurationEdit = {
                    println("on duration edit")
                    item.duration = it
                    onItemEdit(item)
                })
            }
        }
    }
}

