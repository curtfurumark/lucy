package se.curtrune.lucy.screens.duration.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.dev.composables.StatisticsCategory
import se.curtrune.lucy.screens.dev.composables.StatisticsDate
import se.curtrune.lucy.screens.duration.DurationState
import se.curtrune.lucy.screens.duration.SortedBy
import se.curtrune.lucy.screens.duration.data.StatisticItem
import se.curtrune.lucy.statistics.StatisticsPeriod
import se.curtrune.lucy.util.DateTImeConverter
import java.time.LocalDate

@Composable
fun DurationsStatisticsList(state: DurationState) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(state.items){ item->
            when(item){
                is StatisticItem.Category -> {CategoryItem(category = item.category, items = item.items)}
                is StatisticItem.Date -> {DateItem(date = item.date, items = item.items)}
                is StatisticItem.Tag -> {TagItem(tag = item.tag, items = item.items)}
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}
@Composable
fun DateItem(date: LocalDate, items: List<Item>){
    var expanded by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                expanded = !expanded
            }){
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = date.toString())
            Text(text = DateTImeConverter.formatSeconds(items.sumOf { it.duration }))
            if( expanded) {
                items.forEach{
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = it.heading)
                }
            }
        }
    }
}
@Composable
fun CategoryItem(category: String, items: List<Item>) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable {expanded = !expanded}) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = if( category.isNotEmpty() ) category else stringResource(R.string.no_category))
            Text(text = DateTImeConverter.formatSeconds(items.sumOf { it.duration }))
        }
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.fillMaxWidth()) {
                items.forEach {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = it.heading
                    )
                }
            }
        }
    }
}
@Composable
fun TagItem(tag: String, items: List<Item>){
    var expanded by remember {
        mutableStateOf(false)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        Column(
            modifier = Modifier.fillMaxWidth()) {
            Text(text = tag)
            Text(text = DateTImeConverter.formatSeconds(items.sumOf { it.duration }))
        }
        if(expanded){
            items.forEach{
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = it.heading)
            }
        }
    }
}
