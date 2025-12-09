package se.curtrune.lucy.screens.daycalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.composables.PostponeDetails
import se.curtrune.lucy.composables.add_item.DefaultItemSettings
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.modules.PostponeWorker
import se.curtrune.lucy.util.Logger
import java.time.LocalDate
import java.time.LocalTime

class DayCalendarViewModel(val date: LocalDate): ViewModel(){
    private val repository = LucindaApplication.appModule.repository
    private val timeModule = LucindaApplication.appModule.timeModule
    private var currentWeekPage = 5
    private var items: List<Item> = emptyList()
    private val eventChannel = Channel<DayCalendarChannel>()
    val eventFlow = eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(DayCalendarState())
    private var latestDeletedItem: Item? = null
    private val todoRoot: Item? = repository.getTodoRoot()
    val state = _state.asStateFlow()
    val defaultItemSettings = DefaultItemSettings()

    init{
        println("DateViewModel.init date = $date")
        items = repository.selectItems(date)
        println("...number of items ${items.size}")
        _state.update { it.copy(
            items = items,
            date = date,
            tabs = mutableListOf(),
            currentParent = todoRoot
        ) }
        println("currentParent = ${state.value.currentParent?.heading}")
        TopAppbarModule.setTitle(_state.value.date)
    }

    companion object {
        fun factory(date: LocalDate): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DayCalendarViewModel(date) as T
                }
            }
        }
    }

    private fun addItem(item: Item){
        println("...addItem(Item) ${item.heading}")
        Logger.log(item)
        if( item.itemDuration != null) {
            println("...itemDuration: ${item.itemDuration!!.type.name}")
        }else{
            println("...itemDuration is null")
        }
        if( repository.insert(item) == null){
            viewModelScope.launch {
                eventChannel.send(DayCalendarChannel.ShowMessage("error inserting item"))
            }
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
            eventChannel.send(DayCalendarChannel.ConfirmDeleteDialog)
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
/*        _state.update { it.copy(
            editItem =  true,
            currentItem = item
        ) }*/
        viewModelScope.launch{
            eventChannel.send(DayCalendarChannel.EditItem(item))
        }
    }
    private fun hidePostponeDialog(){
        println("hidePostponeDialog()")
        _state.update { it.copy(
            showPostponeDialog = false
        )
        }
    }
    fun onEvent(event: DayCalendarEvent){
        println("DateViewModel.onEvent(${event.toString()})")
        when(event){
            is DayCalendarEvent.AddItem -> {addItem(event.item)}
            is DayCalendarEvent.CurrentDate ->{setCurrentDate(event.date)}
            is DayCalendarEvent.DeleteItem -> {deleteItem(event.item)}
            is DayCalendarEvent.Duplicate -> duplicateItem(event.item)
            is DayCalendarEvent.ShowActionsMenu -> {println("show action menu")}
            is DayCalendarEvent.UpdateItem -> updateItem(event.item)
            is DayCalendarEvent.EditTime -> {updateItem(event.item)}//???
            is DayCalendarEvent.EditItem -> {editItem(event.item)}
            is DayCalendarEvent.ShowPostponeDialog -> { showPostponeDialog(event.item)}
            is DayCalendarEvent.ShowStats -> {showStats(event.item)}
            is DayCalendarEvent.StartTimer -> {startTimer(event.item)}
            is DayCalendarEvent.ShowChildren -> {showChildren(event.item)}
            is DayCalendarEvent.TabSelected -> { tabSelected(event.index, event.item) }
            is DayCalendarEvent.Postpone -> { postpone(event.postponeInfo)}
            is DayCalendarEvent.HidePostponeDialog -> { hidePostponeDialog()}
            is DayCalendarEvent.RestoreDeletedItem -> {restoreDeletedItem()}
            is DayCalendarEvent.Search -> { search(event.filter, event.everywhere)}
            is DayCalendarEvent.Week -> {setCurrentWeek(event.page)}
            is DayCalendarEvent.RequestDelete -> {confirmDelete(event.item)}
            is DayCalendarEvent.ShowAddItemBottomSheet -> {showAddItemBottomSheet()}
        }
    }
    private fun duplicateItem(item: Item){
        println("duplicateItem(${item.heading})")
        val newItem = Item().also {
            it.heading = item.heading
            it.description = item.description
            it.targetDate = item.targetDate
            it.targetTime = item.targetTime
            if( item.parent != null) {
                it.parent = item.parent
            }
            it.category = item.category
        }
        repository.insert(newItem)
        val items = repository.selectItems(date)
        _state.update { it.copy(
            items = items
        ) }

    }
    private fun postpone(postponeDetails: PostponeDetails){
        println("postpone(${postponeDetails.toString()})")
        _state.update { it.copy(
            previousPostponeAmount = postponeDetails.amount
        ) }
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
        //repository

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
        println("DateViewModel.setCurrentDate(${newDate.toString()})")
        items = repository.selectItems(newDate)
        _state.update {it.copy(
                currentWeek = Week(newDate),
                date = newDate,
                items = items
            )
        }
        TopAppbarModule.setTitle(newDate)
    }
    private fun setCurrentWeek(page: Int){
        println("setCurrentWeek(page : $page)")
        if( currentWeekPage == page){
            println("new week same as old week, returning")
            return
        }
        val nWeeks = page - currentWeekPage
        val newDate = _state.value.date.plusWeeks(nWeeks.toLong())
        _state.update { it.copy(
           currentWeek =  it.currentWeek.plusWeek(nWeeks),
            date = newDate,
            items =  repository.selectItems(newDate)
        ) }
        TopAppbarModule.setTitle(newDate)
        currentWeekPage = page
    }
    private fun showAddItemBottomSheet() {
        defaultItemSettings.item = Item().also { item->
            item.targetDate = state.value.date
            item.targetTime = LocalTime.now()
            item.parent = state.value.currentParent
            item.category = state.value.currentParent?.category.toString()
        }
        viewModelScope.launch {
            eventChannel.send(DayCalendarChannel.ShowAddItemBottomSheet)
        }
    }

    private fun showChildren(item: Item){
        println("...showChildren(${item.heading})")
        if( !state.value.showTabs){
            _state.update { it.copy(
                tabs = it.tabs + Item(
                    LocalDate.now().toString()
                )
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