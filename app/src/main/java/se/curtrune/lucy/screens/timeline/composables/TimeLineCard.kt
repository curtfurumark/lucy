package se.curtrune.lucy.screens.timeline.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.item.Item
import java.time.LocalDate

@Composable
fun TimeLineCard(item: Item, onClick: (Item)-> Unit){
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(item) }
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 4.dp, top= 2.dp),
        ) {
            Text(
                modifier= Modifier.padding(start =  4.dp),
                text = item.heading,)
        }
        Row(modifier = Modifier.fillMaxWidth()
            .padding(start= 4.dp , bottom = 2.dp)){
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = item.targetDate.toString(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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