package se.curtrune.lucy.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorCircle(color: Color, onColorClick: (Color)->Unit){
    Box(modifier = Modifier
        .size(50.dp)
        .padding(8.dp)
        .clip(CircleShape)
        .background(color)
        .clickable { onColorClick(color) }
    ){
    }
}

@Composable
fun ColorCircle(color: Color, onClick: ()->Unit){
    Box(modifier = Modifier
        .size(40.dp)
        .padding(8.dp)
        .clip(CircleShape)
        .background(color)
        .clickable { onClick() }
    ){
    }
}