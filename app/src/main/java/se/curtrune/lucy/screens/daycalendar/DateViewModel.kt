package se.curtrune.lucy.screens.daycalendar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item
import java.time.LocalDate

class DateViewModel: ViewModel(){
    private val repository = LucindaApplication.repository
    private val _state = MutableStateFlow(DayCalendarState())
    val state = _state.asStateFlow()
    private lateinit var _tabStack: TabStack

    init{
        println("DateViewModel.init")
        val items = repository.selectItems(_state.value.date)
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
        println("DateViewModel.deleteItem(${item.heading}) (not implemented, not working when implemented)")
//        if( !repository.delete(item)){
//            _state.value.errorMessage = "error deleting ${item.heading}"
//        }
    }

    private fun editItem(item: Item){
        println("...editItem(Item)")
        _state.update { it.copy(
            editItem =  true,
            currentItem = item
        ) }
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
            is DateEvent.PostponeItem -> {postpone(event.item)}
            is DateEvent.ShowPostponeDialog -> {
                _state.update { it.copy(
                    showPostponeDialog = event.show
                ) }
            }

            is DateEvent.ShowStats -> {showStats(event.item)}
            is DateEvent.StartTimer -> {startTimer(event.item)}
            is DateEvent.ShowChildren -> {showChildren(event.item)}
            is DateEvent.TabSelected -> {tabSelected(event.index)}
        }
    }
    private fun postpone(item: Item){
        println("...postpone(${item.heading})")
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