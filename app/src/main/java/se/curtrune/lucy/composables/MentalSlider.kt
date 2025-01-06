package se.curtrune.lucy.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun MentalSlider(level: Int, label: String, onLevelChanged: (Int)->Unit) {
    var position by remember{
        mutableStateOf(level.toFloat())
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "$label ${position.toInt()}")
        Slider(
            value = position,
            onValueChange = {
                position = it
                onLevelChanged(it.toInt())            },
            valueRange = -5f..5f,
            steps = 10
            )
    }
}