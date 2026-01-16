package se.curtrune.lucy.screens.daycalendar.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.app.UserPrefs
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.dialogs.PostponeDialog
import se.curtrune.lucy.composables.item.DateItemCard
import se.curtrune.lucy.screens.daycalendar.DayCalendarEvent
import se.curtrune.lucy.screens.daycalendar.DayCalendarState
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.composables.item.CheckableItemCard


@OptIn(FlowPreview::class)
@Composable
fun CategorizedDayCalendar(
    modifier: Modifier =  Modifier,
    state: DayCalendarState,
    onEvent: (DayCalendarEvent)->Unit,
    onItemEvent: (ItemEvent)->Unit)
{
    println("DayCalendar()")
    val context = LocalContext.current
    val scrollPosition = UserPrefs.getScrollPositionDayCalendar(context)
    Column(modifier = modifier ) {
/*        Search(onSearch = { filter, everywhere ->
            onEvent(DateEvent.Search(filter, everywhere))
        })*/
        val pagerState = rememberPagerState(pageCount = { state.numberWeeks }, initialPage = 5)
        HorizontalPager(state = pagerState) {
            pagerState.settledPage
            onEvent(DayCalendarEvent.Week(pagerState.settledPage))
            DaysOfWeek(state = state, onEvent = { event ->
                onEvent(event)
            })
        }
        if(state.showTabs){
            LazyRow() {
                itemsIndexed(state.tabs){index,  item->
                    MyTab(heading = item.heading, index = index, onEvent = onEvent)
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
            stickyHeader {
                DayCalenderHeader(header = "todo", onClick = {
                    onEvent(DayCalendarEvent.ShowTodoItems(it))
                })
            }
            items(state.todoItems, key = {it.id}){item->
                CheckableItemCard(
                    modifier = Modifier.fillMaxWidth().animateItem(),
                    item= item,
                    onCheckValueChanged = {
                        println("onCheckValueChanged $it, doing nothing")
/*                        item.setIsDone(it) //TODO,feels a bit iffy
                        onItemEvent(ItemEvent.Update(item))*/
                    },
                    onEvent = {
                        onItemEvent(it)
                        println("checkable item card event $it")
                    })
                Spacer(modifier = Modifier.height(4.dp))
            }
            stickyHeader {
                DayCalenderHeader(header = "timed stuff", onClick = {})
            }
            items(state.items, key = {it.id}){ item->
                val swipeState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        println("confirmValueState $it")
                        if( it == SwipeToDismissBoxValue.EndToStart){
                            onEvent(DayCalendarEvent.RequestDelete(item))
                            true
                        }else if (it == SwipeToDismissBoxValue.StartToEnd){
                            onEvent(DayCalendarEvent.ShowPostponeDialog(item))
                            true
                        }
                        println("returning false")
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
                    DateItemCard(
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
            PostponeDialog(
                defaultPostponeAmount = state.previousPostponeAmount,
                onDismiss = {
                    onEvent(DayCalendarEvent.HidePostponeDialog)
            }, onConfirm = { postponeInfo->
                println("onConfirm postpone item ")
                onEvent(DayCalendarEvent.HidePostponeDialog)
                onEvent(DayCalendarEvent.Postpone(postponeInfo))
            },
                item = it
            )
        }
    }
}


@Composable
@Preview
fun PreviewCategorizedDayCalendar(){
    LucyTheme {
        val state = DayCalendarState().also {
            it.items = listOf(Item("hello"), Item("pass"))
        }
        DayCalendar(state = state, onEvent = {})
    }
}
