package se.curtrune.lucy.screens.monthcalendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.Item

@Composable
fun CalendarEvent(event: Item){
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(15.dp),
        //.border(1.dp, color = Color.Green),
        //.background(Color.LightGray),
        //colors = CardDefaults.cardColors(containerColor = Color.Gray),
        //shape = RoundedCornerShape(4.dp)
    ){
        Text(text = event.heading, fontSize = 10.sp, maxLines = 1)
    }
}

@Composable
@Preview
fun PreviewCalendarEvent(){
    LucyTheme {
        CalendarEvent(event = Item("event"))
    }
}