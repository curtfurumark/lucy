package se.curtrune.lucy.screens.daycalendar.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.screens.daycalendar.DayEvent
import se.curtrune.lucy.screens.daycalendar.DayCalendarState


@Composable
fun ListsTabs(state: DayCalendarState ,onEvent: (DayEvent)->Unit){
    var selectedIndex by remember {
        mutableIntStateOf(state.selectedTabIndex)
    }
    println("list tabs selected tab index: ${state.selectedTabIndex}")
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        modifier = Modifier.fillMaxWidth()){
        //tab for parent list, go back to parent tab
        Tab(
            modifier = Modifier.padding(end = 8.dp),
            selected = selectedIndex == 0, onClick = {
            onEvent(DayEvent.TabSelected(0))
        }){
            Text(text = state.tabStack?.date.toString())
        }
        state.tabStack?.items?.forEachIndexed{ index, item->
            Tab(selected = selectedIndex == index, onClick = {
                selectedIndex = index + 1
                onEvent(DayEvent.TabSelected(selectedIndex))
            }){
                Text(text = item.heading)
            }

        }

/*        }
        tabs.forEachIndexed{index, title->
            Tab(selected =  selectedIndex == index, onClick = {
                selectedIndex = index
                onEvent(DateEvent.TabSelected(index))
                println(" on tab click")
            }){
                Text(text = title)
            }
        }*/
    }
}