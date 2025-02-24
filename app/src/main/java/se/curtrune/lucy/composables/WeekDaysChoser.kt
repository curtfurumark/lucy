package se.curtrune.lucy.composables

import android.graphics.drawable.shapes.RoundRectShape
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.screens.daycalendar.composables.DayOfWeek
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekDayChooser(onSelect: (DayOfWeek, Boolean)->Unit){
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        java.time.DayOfWeek.entries.forEach { day ->
            println("day: ${day.name}")
            //Text(text = day.name)
            WeekDay(weekDay = day, onSelect = onSelect)
        }
    }
}

@Composable
fun WeekDay( weekDay: DayOfWeek, onSelect: (DayOfWeek, Boolean) -> Unit){
    var selected by remember{
        mutableStateOf(false)
    }
    //Surface(modifier = Modifier.) { }
    Box(modifier = Modifier.clip(shape = CircleShape)
        .size(20.dp)
        .clickable {
            selected = !selected
            onSelect(weekDay, selected) }
        .background(color = if( selected) Color.Blue else Color.Transparent)
        .border(Dp.Hairline, color = if( selected) Color.Black else Color.Transparent),
        contentAlignment = Alignment.Center){
        Text(text = weekDay.getDisplayName(TextStyle.NARROW, Locale.getDefault()))
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewWeekDayChooser(){
    WeekDayChooser(onSelect = {day, selected -> {

    }})
}