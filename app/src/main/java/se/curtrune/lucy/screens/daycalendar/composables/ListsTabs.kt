package se.curtrune.lucy.screens.daycalendar.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.daycalendar.DateEvent
import se.curtrune.lucy.screens.daycalendar.DayCalendarState


@Composable
fun ListsTabs(state: DayCalendarState ,onEvent: (DateEvent)->Unit){
    val tabs = listOf(state.date.toString(), "shopping", "pizza")
    var selectedIndex by remember {
        mutableIntStateOf(1)
    }
    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = Modifier.fillMaxWidth()){
        Tab(selected = selectedIndex == 0, onClick = {
            onEvent(DateEvent.TabSelected(0))
        }){
            Text(text = state.tabStack?.date.toString())
        }
        state.tabStack?.items?.forEachIndexed{ index, item->
            Tab(selected = selectedIndex == index, onClick = {
                selectedIndex = index + 1
                onEvent(DateEvent.TabSelected(selectedIndex))
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