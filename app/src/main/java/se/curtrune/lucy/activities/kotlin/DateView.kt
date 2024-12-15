package se.curtrune.lucy.activities.kotlin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.Item
import java.time.LocalDate

@Composable
fun DateView(date: LocalDate, events: List<Item>){
    println("DateView() $date")
    Box(
        modifier = Modifier
            .border(BorderStroke(Dp.Hairline, Color.Blue))
            .aspectRatio(0.5f)
            .clip(RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.TopCenter

    ){
        Column( modifier = Modifier.padding(2.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = date.dayOfMonth.toString(), fontSize = 14.sp, color = Color.White)
            for(event in events){
                CalendarEvent(event = event)
            }
        }
    }
}
@Composable
fun CalendarEvent(event: Item){
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(), shape = RoundedCornerShape(2.dp)){
        Text(text = "${event.heading}", fontSize = 10.sp, color = Color.Yellow, maxLines = 1)
    }

}