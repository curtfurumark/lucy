package se.curtrune.lucy.screens.dev.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.util.DateTImeFormatter
import java.time.LocalDate

@Composable
fun StatisticsDate(modifier: Modifier = Modifier, date: LocalDate, items: List<Item>) {
    var itemsVisible by remember{
        mutableStateOf(false)
    }
    Text(text = date.toString(), fontSize = 20.sp,
        modifier = Modifier.clickable {
            itemsVisible = !itemsVisible
        })
    Text(text = DateTImeFormatter.formatSeconds(items.sumOf { item -> item.duration }))
    AnimatedVisibility(visible =  itemsVisible) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            items.forEach { item->
                Text(text = "${item.heading} ${DateTImeFormatter.formatSeconds(item.duration)}",
                    modifier = Modifier.padding(start = 16.dp))
            }
        }
    }
}
