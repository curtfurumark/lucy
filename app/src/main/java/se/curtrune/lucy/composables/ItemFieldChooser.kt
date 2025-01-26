package se.curtrune.lucy.composables

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class Field{
    ENERGY, ANXIETY, STRESS, MOOD, DURATION
}

@Composable
fun ItemFieldChooser(onFieldChosen: (Field)->Unit){
    println("ItemFieldChooser()")
    var selectedField by remember {
        mutableStateOf(Field.ENERGY)
    }
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically){
        Field.entries.forEach {
            println("entry: ${it.name}")
            RadioButton(onClick = {
                onFieldChosen(it)
                selectedField = it
            }, selected = selectedField == it)
            Text(text = it.toString(), color = Color.White)
        }
    }

}