package se.curtrune.lucy.screens.daycalendar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.composables.PostponeDetails
import se.curtrune.lucy.modules.PostponeWorker
import se.curtrune.lucy.util.Logger
import java.time.LocalDate
import java.util.Comparator

class DateViewModel: ViewModel(){
    private val repository = LucindaApplication.repository
    //private var items: MutableList<Item> = mutableListOf()
    private var items: List<Item> = emptyList()
    private val _state = MutableStateFlow(DayCalendarState())
    private var latestDeletedItem: Item? = null
    val state = _state.asStateFlow()
    private lateinit var _tabStack: TabStack

    init{
        println("DateViewModel.init")
        items = repository.selectItems(_state.value.date)
        _state.value.items = items
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
    fun onEvent(event: DateEvent){
        println("DateViewModel.onEvent(${event.toString()})")
        when(event){
            is DateEvent.AddItem -> {addItem(event.item)}
            is DateEvent.CurrentDate ->{setCurrentDate(event.date)}
            is DateEvent.DeleteItem -> {deleteItem(event.item)}
            is DateEvent.ShowActionsMenu -> {println("show action menu")}
            is DateEvent.UpdateItem -> updateItem(event.item)
            is DateEvent.EditTime -> {updateItem(event.item)}
            is DateEvent.EditItem -> {editItem(event.item)}
            is DateEvent.ShowPostponeDialog -> { showPostponeDialog(event.item)}
            is DateEvent.ShowStats -> {showStats(event.item)}
            is DateEvent.StartTimer -> {startTimer(event.item)}
            is DateEvent.ShowChildren -> {showChildren(event.item)}
            is DateEvent.TabSelected -> {tabSelected(event.index)}
            is DateEvent.Postpone -> { postpone(event.postponeInfo)}
            is DateEvent.HidePostponeDialog -> { hidePostponeDialog()}
            is DateEvent.RestoreDeletedItem -> {restoreDeletedItem()}
            is DateEvent.Search -> { search(event.filter, event.everywhere)}
        }
    }
    private fun postpone(postponeDetails: PostponeDetails){
        println("postpone(PostponeDetails)")
        if( postponeDetails.postPoneAll){
            println("postpone item and all items after")
            //val filteredItems = state.value.items.filter {  }
        }else{
            println(" postpone single item")
            if( postponeDetails.item != null){
                val item  = PostponeWorker.postponeItem(postponeDetails.item!!, postponeDetails.amount )
                Logger.log(item)
                repository.update(item)
                _state.update { it.copy(
                    items = repository.selectItems(state.value.date)
                ) }
            }else{
                println("error")
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
        val filteredItems: List<Item> = items.filter { item-> item.contains(filter)  }
        _state.update { it.copy(
            items = filteredItems
        ) }

    }
    private fun setCurrentDate(newDate: LocalDate){
        _state.update {it.copy(
            date = newDate,
            items = repository.selectItems(newDate)
        )
        }
    }
    private fun showChildren(item: Item){
        println("...showChildren(${item.heading})")
        if( !state.value.showTabs){
            _tabStack = TabStack(state.value.date)
            //tabStack.pushItem(item)
        }
        _tabStack.pushItem(item)
        _state.update { it.copy(
           items = repository.selectChildren(item),
            currentParent = item,
            showTabs = true,
            tabStack = _tabStack
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
    private fun startTimer(item: Item){
        println("startTime(${item.heading})")
    }
    private fun tabSelected(index: Int){
        println("tabSelected($index)")
        if( index == 0){
            _state.update {  it.copy(
                items = repository.selectItems(state.value.date),
                showTabs = false
            )}
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