package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.classes.item.Item

@Composable
fun MentalCard(modifier: Modifier = Modifier, item: Item, onMentalChanged: (Mental)->Unit){
    var energy by remember { mutableStateOf(item.energy) }
    var mood by remember { mutableStateOf(item.mood) }
    var anxiety by remember { mutableStateOf(item.anxiety) }
    var stress by remember { mutableStateOf(item.stress) }
    var visible by remember { mutableStateOf(false) }
    var mental by remember { mutableStateOf(item.mental) }
    Card(modifier = modifier.fillMaxWidth()
    ){
        Row(modifier = modifier.fillMaxWidth()
            .clickable(onClick = { visible = !visible }),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
            Text(
                modifier = Modifier.padding(8.dp),
                text = "mental")
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription ="view mental")
        }
        AnimatedVisibility(visible = visible) {
            Column(modifier = Modifier.padding(8.dp)) {
                MentalSlider(label = "energy", initialValue = energy) {
                    mental.energy = it
                    onMentalChanged(mental)
                }
                MentalSlider(label = "mood", initialValue = mood) {
                    mental.mood = it
                    onMentalChanged(mental)
                }
                MentalSlider(label = "anxiety", initialValue = anxiety) {
                    mental.anxiety = it
                    onMentalChanged(mental)
                }
                MentalSlider(label = "stress", initialValue = stress) {
                    mental.stress = it
                    onMentalChanged(mental)
                }
            }
        }
    }
}
@Composable
fun MentalSlider(label: String, initialValue: Int, onValueChange: (Int)->Unit){
    var level by remember { mutableStateOf(initialValue) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "$label $level")
        Slider(
            value = level.toFloat(),
            onValueChange = {
                level = it.toInt()
                onValueChange(it.toInt())
            },
            steps = 10,
            valueRange = -5f..5f
        )
    }
}

@Composable
@Preview
fun PreviewMentalCard(){
    val mental = Mental(10, 10, 10, 10)
    val item = Item().also{
        it.energy = mental.energy
        it.mood = mental.mood
        it.anxiety = mental.anxiety
        it.stress = mental.stress
    }
    MentalCard(item = item, onMentalChanged = {})

}