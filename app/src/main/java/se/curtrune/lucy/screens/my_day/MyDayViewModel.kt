package se.curtrune.lucy.screens.my_day

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.Field
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.workers.MentalWorker
import java.time.LocalDate

class MyDayViewModel(val date: LocalDate, savedStateHandle: SavedStateHandle) : ViewModel() {
    private val mentalModule = LucindaApplication.appModule.mentalModule
    private val repository = LucindaApplication.appModule.repository
    private var allItems: List<Item> = mutableListOf()
    private val _myDayChannel = Channel<MyDayChannel>()
    val myDayChannel = _myDayChannel.receiveAsFlow()
    private var filteredItems: List<Item> = emptyList()
    private var _state = MutableStateFlow(MyDayState())
    var state = _state.asStateFlow()
    init{
        println("init{ date: $date}")
        allItems = repository.selectItems(date)
        _state.update { it.copy(
            items = allItems,
            date = date,
            mental = MentalWorker.getMental(allItems)
        ) }
        TopAppbarModule.setTitle("min dag")
    }
    fun onEvent(event: MyDayEvent){
        println("MentalDateViewModel.onEvent(MentalEvent)")
        when(event){
            is MyDayEvent.Date -> {
                setDate(event.date)
            }
            is MyDayEvent.SetEditField -> TODO()
            is MyDayEvent.UpdateItem -> {
                updateItem(event.item)
            }

            is MyDayEvent.AllDay -> { setAllDay(event.allDay)}
            is MyDayEvent.ShowDatePicker ->  {
                println("show date picker")
            }

            is MyDayEvent.Field -> { setField(event.field)}
        }

    }
    private fun setDate(date: LocalDate) {
        println("MentalDateViewModel.setDate(${date.toString()})")
        _state.update { it.copy(
            items = repository.selectItems(date),
            date = date,
            mental = MentalWorker.getMental(it.items)
        ) }
    }
    private fun setField(field: Field) {
        println("setField(${field.name})")
        _state.update {
            it.copy(
                currentField = field
            )
        }
    }
    private fun updateItem(item: Item){
        println("....updateItem(${item.heading}), energy: ${item.energy}")
        val rowsAffected = repository.update(item)
        if( rowsAffected != 1){
            viewModelScope.launch{
                _myDayChannel.send(MyDayChannel.ShowMessage("error updating item"))
            }
            return
        }
        _state.update {
            it.copy(
                mental = mentalModule.getCurrentMental()
            )
        }
    }
    private fun setAllDay(allDay: Boolean) {
        println("...setAllDay($allDay)")
        if( allDay){
            _state.update { it.copy(
                items = allItems
            ) }
        }else{
            _state.update {it.copy(
                items = allItems.filter { item->item.isDone }
                )
            }
        }
    }
}

class MyDayViewModelFactory(
    private val date: LocalDate,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(MyDayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyDayViewModel(date, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
