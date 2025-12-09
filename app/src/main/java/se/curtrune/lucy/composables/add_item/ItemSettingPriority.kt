package se.curtrune.lucy.composables.add_item

import androidx.collection.mutableIntSetOf
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSettingPriority(priority: Int, onPriorityChanged: (Int) -> Unit) {
    var priority by remember { mutableIntStateOf(priority) }
    var sliderVisible by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable { sliderVisible = !sliderVisible },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "priority: $priority"
            )
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "set priority")
        }
        AnimatedVisibility(visible = sliderVisible) {
            Slider(
                value = priority.toFloat(),
                onValueChange = {
                    priority = it.toInt()
                    onPriorityChanged(it.toInt())
                },
                valueRange = 0f..5f,
                steps = 5,
            )
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewItemSettingPriority() {
    ItemSettingPriority(priority = 1, onPriorityChanged = {})
}