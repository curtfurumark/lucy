package se.curtrune.lucy.screens.monthcalendar.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item

@Composable
fun CalendarEvent(event: Item){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    ){
        Text(
            text = event.heading,
            style = TextStyle(platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Unspecified,
            fontSize = 10.sp,
            maxLines = 1)
    }
}

@Composable
@PreviewLightDark
@Preview(showBackground = true)
fun PreviewCalendarEvent(){
    LucyTheme {
        CalendarEvent(event = Item("event"))
    }
}