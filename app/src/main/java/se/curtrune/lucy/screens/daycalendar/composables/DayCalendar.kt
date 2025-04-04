package se.curtrune.lucy.screens.daycalendar.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import se.curtrune.lucy.app.UserPrefs
import se.curtrune.lucy.composables.PostponeDialog
import se.curtrune.lucy.screens.daycalendar.DayEvent
import se.curtrune.lucy.screens.daycalendar.DayCalendarState
import se.curtrune.lucy.screens.dev.composables.MyTab
import java.time.LocalDate


@OptIn(FlowPreview::class)
@Composable
fun DayCalendar(state: DayCalendarState, onEvent: (DayEvent)->Unit){
    val context = LocalContext.current
    val scrollPosition = UserPrefs.getScrollPositionDayCalendar(context)
    Column() {
/*        Search(onSearch = { filter, everywhere ->
            onEvent(DateEvent.Search(filter, everywhere))
        })*/
        val pagerState = rememberPagerState(pageCount = { state.numberWeeks }, initialPage = 5)
        HorizontalPager(state = pagerState) {
            pagerState.settledPage
            onEvent(DayEvent.Week(pagerState.settledPage))
            DaysOfWeek(state = state, onEvent = { event ->
                onEvent(event)
            })
        }
        if(state.showTabs){
            LazyRow() {
                itemsIndexed(state.tabs){index,  item->
                    MyTab(heading = item.heading, index = index, onEvent = onEvent)
                    //Text(text = item.heading)
                }
            }
        }
        val lazyListState = rememberLazyListState(
            //initialFirstVisibleItemIndex = scrollPosition
        )
        LaunchedEffect(lazyListState) {
            snapshotFlow {
                lazyListState.firstVisibleItemIndex
            }
                .debounce(500L)
                .collectLatest { index ->
                    println("index of first visible item: $index")
                    UserPrefs.setScrollPositionDayCalender(index, context)
                }
        }
        LazyColumn(state = lazyListState) {
            items(state.items, key = {it.id}){ item->
                val swipeState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if( it == SwipeToDismissBoxValue.EndToStart){
                            onEvent(DayEvent.RequestDelete(item))
                            true
                        }else if (it == SwipeToDismissBoxValue.StartToEnd){
                            onEvent(DayEvent.ShowPostponeDialog(item))
                            true
                        }
                        false
                    }, positionalThreshold = {it * 0.5f}
                )
                SwipeToDismissBox(
                    modifier = Modifier.animateItem(),
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
                onEvent(DayEvent.HidePostponeDialog)
            }, onConfirm = { postponeInfo->
                println("onConfirm postpone item ")
                onEvent(DayEvent.HidePostponeDialog)
                onEvent(DayEvent.Postpone(postponeInfo))
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
