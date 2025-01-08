package se.curtrune.lucy.activities.kotlin.daycalendar

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
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
    fun onEvent(event: DateEvent){
        println("DateViewModel.onEvent(DateEvent) ${event.toString()}")
        when(event){
            is DateEvent.AddItem -> {println("add item")}
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
            is DateEvent.EditTime -> {println("edit time")}
            else ->{ println("or else !!")
            }
        }
    }
    private fun updateItem(item: Item){
        println("...updateItem(${item.heading})")
        val rowsAffected = ItemsWorker.update(item, context)
        if( rowsAffected != 1){
            println("error updating item")
        }
    }
}