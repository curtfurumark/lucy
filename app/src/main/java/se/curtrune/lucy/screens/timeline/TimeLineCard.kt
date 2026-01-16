package se.curtrune.lucy.screens.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import se.curtrune.lucy.classes.item.Item
import java.time.LocalDate

@Composable
fun TimeLineCard(item: Item, onClick: (Item)-> Unit){
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(item) }
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            //horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.heading,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = item.targetDate.toString())
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewTimeLineCard(){
    val item = Item(
        heading = "test långt jkjkjk nnamn som bekjkj  jkjikjikjkjk jkjkjkjikjjkjkjkjkjkjkjkjkjk")
    item.targetDate = LocalDate.of(2019,8, 31)
    TimeLineCard(item = item, onClick = {})
}