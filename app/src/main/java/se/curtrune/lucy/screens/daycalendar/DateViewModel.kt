package se.curtrune.lucy.screens.daycalendar

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.workers.ItemsWorker

class DateViewModel(private val context: Context): ViewModel(){
    private val _state = MutableStateFlow(DayCalendarState())
    val state = _state.asStateFlow()

    init{
        println("DateViewModel.init")
        val items = ItemsWorker.selectItems(_state.value.date, context)
        _state.value.items = items
    }
    private fun addItem(item: Item){
        println("...addItem(Item) ${item.heading}")
        val itemWithId = ItemsWorker.insert(item, context)
        _state.update { it.copy(
            //items = it.items + itemWithId
            items = ItemsWorker.selectItems(state.value.date, context)
        ) }
    }
    private fun editItem(item: Item){
        println("...editItem(Item)")
        _state.update { it.copy(
            editItem =  true,
            currentItem = item
        ) }
    }
    fun onEvent(event: DateEvent){
        println("DateViewModel.onEvent(DateEvent) ${event.toString()}")
        when(event){
            is DateEvent.AddItem -> {addItem(event.item)}
            is DateEvent.CurrentDate -> {
                _state.update {it.copy(
                    date = event.date,
                    items = ItemsWorker.selectItems(event.date, context)
                )
                }
            }
            is DateEvent.DeleteItem -> {println("delete item")}
            is DateEvent.EditItem -> { println("edit item") }
            is DateEvent.ShowActionsMenu -> {println("show action menu")}
            is DateEvent.UpdateItem -> updateItem(event.item)
            is DateEvent.EditTime -> {editItem(event.item)}
        }
    }
    private fun updateItem(item: Item){
        println("...updateItem(${item.heading})")
        val rowsAffected = ItemsWorker.update(item, context)
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