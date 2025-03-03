package se.curtrune.lucy.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.skydoves.colorpickerview.ColorPickerView
import se.curtrune.lucy.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPicker(dismiss: ()->Unit, onColor: (Color)->Unit){
    val colorList = listOf(
        Color.LightGray, Color.DarkGray, Color.Gray, Color.Red, Color.Green, Color.Blue,
        colorResource(R.color.light_brown),
        colorResource(R.color.brown),
        colorResource(R.color.dark_brown),
        colorResource(R.color.ochre),
        colorResource(R.color.pink),
        colorResource(R.color.mental_green),
        colorResource(R.color.quote)
    )

    Dialog(onDismissRequest = dismiss) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ){
            Row(modifier = Modifier.fillMaxWidth()
                , horizontalArrangement = Arrangement.Center
            ){
                Text(text = "choose a colour", fontSize = 24.sp)
            }
            FlowRow( modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                colorList.forEach { color ->
                    ColorBox(color, onColorClick = { onColor(color) })
                }
            }
        }
    }
}

@Composable
fun ColorBox(color: Color, onColorClick: (Color)->Unit){
    Box(modifier = Modifier
        .size(50.dp)
        .padding(8.dp)
        .clip(CircleShape)
        .background(color)
        .clickable { onColorClick(color) }
    ){
    }
}