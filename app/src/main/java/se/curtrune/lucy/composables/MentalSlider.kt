package se.curtrune.lucy.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import se.curtrune.lucy.screens.log_in.ui.theme.LucyTheme

@Composable
fun MentalSlider(level: Int, label: String, onLevelChanged: (Int)->Unit) {
    var position by remember{
        mutableStateOf(level.toFloat())
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label ${position.toInt()}",
            color = MaterialTheme.colorScheme.primary)
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

@Composable
@PreviewLightDark
fun PreviewMentalSlider(){
    LucyTheme {
        MentalSlider(level = 0, label = "energy") {
        }
    }
}