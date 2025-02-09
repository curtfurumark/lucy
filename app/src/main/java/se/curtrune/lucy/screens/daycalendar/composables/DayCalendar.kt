package se.curtrune.lucy.screens.daycalendar.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.composables.PostponeDialog
import se.curtrune.lucy.composables.Search
import se.curtrune.lucy.screens.daycalendar.DateEvent
import se.curtrune.lucy.screens.daycalendar.DayCalendarState
import java.time.LocalDate


@Composable
fun DayCalendar(state: DayCalendarState, onEvent: (DateEvent)->Unit){
    val context = LocalContext.current
    Column() {
        Search(onSearch = { filter, everywhere ->
            onEvent(DateEvent.Search(filter, everywhere))
        })
        val pagerState = rememberPagerState(pageCount = { 10 }, initialPage = 5)
        HorizontalPager(state = pagerState) {
            pagerState.settledPage
            DaysOfWeek(state = state, onEvent = { event ->
                onEvent(event)
            })
        }
        if(state.showTabs){
            ListsTabs(state = state, onEvent = onEvent)
        }
        LazyColumn {
            items(state.items, key = {it.id}){ item->
                val swipeState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if( it == SwipeToDismissBoxValue.EndToStart){
                            onEvent(DateEvent.DeleteItem(item))
                            true
                        }else if (it == SwipeToDismissBoxValue.StartToEnd){
                            //onEvent(DateEvent.PostponeItem(state.items[index]))
                            onEvent(DateEvent.ShowPostponeDialog(item))
                            true
                        }
                        false
                    }, positionalThreshold = {it * 0.25f}
                )
                SwipeToDismissBox(
                    state = swipeState,
                    backgroundContent = {
                        SwipeBackground(state = swipeState)
                    }
                ) {
                    DateItem(
                        modifier = Modifier.fillMaxWidth()
                            .animateItem(),
                        item, onEvent = {
                        onEvent(it)
                    })
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
    if(state.showPostponeDialog){
        state.currentItem?.let {
            PostponeDialog(onDismiss = {
                onEvent(DateEvent.HidePostponeDialog)
            }, onConfirm = { postponeInfo->
                println("onConfirm postpone item ")
                onEvent(DateEvent.HidePostponeDialog)
                onEvent(DateEvent.Postpone(postponeInfo))
            },
                item = it
            )
        }
    }
}
@Composable
fun SwipeBackground(state: SwipeToDismissBoxState){
    val color = when(state.dismissDirection){
        SwipeToDismissBoxValue.StartToEnd -> Color.Green
        SwipeToDismissBoxValue.EndToStart -> Color.Red
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }
    Row(
        modifier = Modifier.fillMaxSize()
        .background(color),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(imageVector = Icons.Default.Delete, contentDescription = "delete", tint = Color.Black)
        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "postpone", tint = Color.Black)
    }
}

@Composable
fun Header(){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        Text(text = LocalDate.now().toString())
    }
}