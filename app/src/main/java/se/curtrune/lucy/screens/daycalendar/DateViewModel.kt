package se.curtrune.lucy.screens.daycalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.composables.PostponeDetails
import se.curtrune.lucy.modules.PostponeWorker
import java.time.LocalDate

class DateViewModel: ViewModel(){
    private val repository = LucindaApplication.repository
    private val timeModule = LucindaApplication.timeModule
    private var currentWeekPage = 5
    private var items: List<Item> = emptyList()
    private val eventChannel = Channel<DayChannel>()
    val eventFlow = eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(DayCalendarState())
    private var latestDeletedItem: Item? = null
    private val todoRoot: Item = repository.getTodoRoot()
    val state = _state.asStateFlow()
    //private lateinit var _tabStack: TabStack

    init{
        println("DateViewModel.init")
        items = repository.selectItems(_state.value.date)
        _state.value.items = items
        _state.value.currentParent = todoRoot
    }
    private fun addItem(item: Item){
        println("...addItem(Item) ${item.heading}")
        if( repository.insert(item) == null){
            println("error inserting item")
            return
        }
        _state.update { it.copy(
            items = repository.selectItems(state.value.date)
        ) }
    }
    private fun confirmDelete(item: Item){
        println("...confirmDelete(${item.heading})")
        _state.update { it.copy(
            currentItem =  item
        ) }
        viewModelScope.launch{
            eventChannel.send(DayChannel.ConfirmDeleteDialog)
        }
    }
    private fun deleteItem(item: Item){
        println("DateViewModel.deleteItem(${item.heading}")
        latestDeletedItem = item
        if( !repository.delete(item)){
            println("error deleting item")
            _state.value.errorMessage = "error deleting ${item.heading}"
        }else{
            println("item deleted ok")
            _state.update { it.copy(
                items = it.items.minus(item)
            ) }
        }
    }

    private fun editItem(item: Item){
        println("...editItem(Item)")
        _state.update { it.copy(
            editItem =  true,
            currentItem = item
        ) }
    }
    private fun hidePostponeDialog(){
        println("hidePostponeDialog()")
        _state.update { it.copy(
            showPostponeDialog = false
        )
        }
    }
    fun onEvent(event: DayEvent){
        println("DateViewModel.onEvent(${event.toString()})")
        when(event){
            is DayEvent.AddItem -> {addItem(event.item)}
            is DayEvent.CurrentDate ->{setCurrentDate(event.date)}
            is DayEvent.DeleteItem -> {deleteItem(event.item)}
            is DayEvent.ShowActionsMenu -> {println("show action menu")}
            is DayEvent.UpdateItem -> updateItem(event.item)
            is DayEvent.EditTime -> {updateItem(event.item)}
            is DayEvent.EditItem -> {editItem(event.item)}
            is DayEvent.ShowPostponeDialog -> { showPostponeDialog(event.item)}
            is DayEvent.ShowStats -> {showStats(event.item)}
            is DayEvent.StartTimer -> {startTimer(event.item)}
            is DayEvent.ShowChildren -> {showChildren(event.item)}
            is DayEvent.TabSelected -> { tabSelected(event.index, event.item) }
            is DayEvent.Postpone -> { postpone(event.postponeInfo)}
            is DayEvent.HidePostponeDialog -> { hidePostponeDialog()}
            is DayEvent.RestoreDeletedItem -> {restoreDeletedItem()}
            is DayEvent.Search -> { search(event.filter, event.everywhere)}
            is DayEvent.Week -> {setCurrentWeek(event.page)}
            is DayEvent.RequestDelete -> {confirmDelete(event.item)}
        }
    }
    private fun postpone(postponeDetails: PostponeDetails){
        println("postpone(${postponeDetails.toString()})")
        if( postponeDetails.postPoneAll){
            val postponeItem = postponeDetails.item ?: return
            val postponeAmount = postponeDetails.amount
            println("postpone item and all items after")
            val filteredItems = state.value.items.filter { item-> item.targetTime.isAfter(postponeItem.targetTime) }
            filteredItems.forEach{ item->
                println(item.toString())
                repository.update(PostponeWorker.postponeItem(item, amount = postponeAmount ))
                if( item.targetDate != state.value.date){
                    _state.update { it.copy(
                        items = it.items.minus(item)
                    ) }
                }
            }
            //sort list, needed or not, better safe than sorry
            sortItems()
            repository.update(postponeItem)
        }else{
            println(" postpone single item")
            val postponeItem = postponeDetails.item?:return
            repository.update(PostponeWorker.postponeItem(postponeItem, postponeDetails.amount))
            //if item has been postponed out of current date
            if(postponeItem.targetDate != state.value.date){
                _state.update { it.copy(
                    items = it.items.minus(postponeItem)
                ) }
            }else{
                sortItems()
            }
        }
    }
    private fun restoreDeletedItem(){
        println("restoreDeletedItem")
        //repository.restore()
        repository

    }
    private fun search(filter: String, everywhere: Boolean){
        println("DateViewModel.search($filter, everywhere: $everywhere)")
        if( everywhere){
            if( filter.isEmpty()){
                _state.update {it.copy(
                    items = items
                )
                }
            }else {
                val filteredItems = repository.search(filter)
                _state.update {
                    it.copy(
                        items = filteredItems
                    )
                }
            }
        }else {
            val filteredItems: List<Item> = items.filter { item -> item.contains(filter) }
            _state.update {
                it.copy(
                    items = filteredItems
                )
            }
        }

    }
    fun setCalendarDate(calendarDate: CalenderDate) {
        println("DateViewModel.setCalendarDate(${calendarDate.date.toString()})")
        setCurrentDate(calendarDate.date)

    }
    private fun setCurrentDate(newDate: LocalDate){
        _state.update {it.copy(
            currentWeek = Week(newDate),
            date = newDate,
            items = repository.selectItems(newDate)
            )
        }
    }
    private fun setCurrentWeek(page: Int){
        println("setCurrentWeek(page : $page)")
        if( currentWeekPage == page){
            println("new week same as old week, returning")
            return
        }
        val nWeeks = page - currentWeekPage
       // val newWeek = state.value.currentWeek.plusWeek(nWeeks)
        _state.update { it.copy(
           currentWeek =  it.currentWeek.plusWeek(nWeeks),
            date = it.date.plusWeeks(nWeeks.toLong())
        ) }
        currentWeekPage = page
    }
    private fun getWeek(){

    }
    private fun showChildren(item: Item){
        println("...showChildren(${item.heading})")
        if( !state.value.showTabs){
            _state.update { it.copy(
                tabs = it.tabs +  Item(LocalDate.now().toString())
            ) }
        }
        _state.update { it.copy(
           items = repository.selectChildren(item),
            currentParent = item,
            showTabs = true,
            tabs = it.tabs + item,
            ) }
    }
    private fun showPostponeDialog(item: Item){
        _state.update { it.copy(
            showPostponeDialog = true,
            currentItem = item
        ) }
    }
    private fun showStats(item: Item){
        _state.update { it.copy(
            currentItem =   item,
            showStats =  true
        ) }
    }
    private fun sortItems(){
        _state.update { it.copy(
            items = it.items.sortedBy { it.targetTime }
        ) }
    }
    private fun startTimer(item: Item){
        println("startTimer(${item.heading})")
        timeModule.startTimer(item.id)
    }
    private fun tabSelected(index : Int, item: Item?){
        println("tabSelected($index)")
        if( index == 0){
            _state.update {  it.copy(
                items = repository.selectItems(state.value.date),
                showTabs = false,
                tabs = mutableListOf<Item>(),
                currentParent = todoRoot
            )}
        }else{
            println("item tab selected")
            val parentItem = state.value.tabs[index]
            if( index < state.value.tabs.size){
                println("remove tabs to the right of selected item")
            }
            println("parentItem: ${parentItem.heading}")
            _state.update { it.copy(
                currentParent = parentItem,
                items = repository.selectChildren(parentItem)
            ) }
        }
    }
    private fun updateItem(item: Item){
        println("...updateItem(${item.heading})")
        val rowsAffected = repository.update(item)
        if( rowsAffected != 1){
            println("error updating item")
        }
        _state.update { state ->
            state.copy(
                items = state.items.sortedBy { it.targetTime }
        ) }
    }
    private fun sortItems(items: List<Item>): List<Item>{
        return items.sortedBy { it.targetTime }
    }


}