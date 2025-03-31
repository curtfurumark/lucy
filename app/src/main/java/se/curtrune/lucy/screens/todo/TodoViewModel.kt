package se.curtrune.lucy.screens.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.composables.top_app_bar.TopAppBarEvent
import se.curtrune.lucy.modules.MainModule
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.util.Logger
import java.util.Comparator

class TodoViewModel : ViewModel() {
    private val _state = MutableStateFlow(TodoState())
    val state = _state.asStateFlow()
    private val repository = LucindaApplication.appModule.repository
    private var items: MutableList<Item> = mutableListOf()
    private val eventChannel = Channel<ChannelEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun delete(item: Item) {
        println("TodoViewModel(${item.heading})")
        val stat = repository.delete(item)
        if (!stat) {
            println("ERROR deleting item")
            showMessage("error deleting item: ${item.heading}")
        } else {
            showProgressBar(true)
            _state.update { it.copy(
                items =  repository.selectItems(State.TODO)
            ) }
            showProgressBar(false)
        }
    }

    fun filter(filter: String?, everywhere: Boolean) {
        val filteredItems = items.filter { item: Item -> item.contains(filter)  }
        _state.update { it.copy(
            items = filteredItems
        ) }
    }

/*    fun getItem(index: Int): Item {
        return mutableItems.value!![index]
    }*/
    init {
        items = repository.selectItems(State.TODO).toMutableList()
        items.sortWith(Comparator.comparingLong() { it.compare() })
        _state.update { it.copy(
            items = items
        ) }
        MainModule.setTitle("todo/att göra")
    }
    private fun editItem(item: Item){
        viewModelScope.launch {
            eventChannel.send(ChannelEvent.Edit(item))
        }
    }

    private fun insert(item: Item) {
        //var item = item
        println("TodoFragmentViewModel.insert(Item)")
        val itemWithID = repository.insert(item)
        //mutableItems.value!!.add(itemWithID)
        sort()
    }

    private fun sort() {
        //mutableItems.value!!.sortWith(Comparator.comparingLong { obj: Item -> obj.compare() })
    }
    fun onEvent(event: ItemEvent){
        when(event){
            is ItemEvent.Delete -> {delete(event.item)}
            is ItemEvent.Edit -> { editItem(event.item)}
            is ItemEvent.Update -> {update(event.item)}
            is ItemEvent.CancelTimer -> {}
            is ItemEvent.GetChildren -> {}
            is ItemEvent.GetChildrenType -> {}
            is ItemEvent.GetItem -> {}
            is ItemEvent.PauseTimer -> {}
            is ItemEvent.ResumeTimer -> {}
            is ItemEvent.StartTimer -> {}
            is ItemEvent.InsertItem -> {}
            is ItemEvent.ShowAddItemDialog -> {showAddItemDialog()}
            is ItemEvent.InsertChild -> {
                //TODO, implement this
                println("insert child TODO")
            }
        }
    }
    fun onEvent(event: TopAppBarEvent){
        when(event){
            TopAppBarEvent.DayCalendar -> TODO()
            TopAppBarEvent.DrawerMenu -> TODO()
            TopAppBarEvent.OnBoost -> TODO()
            TopAppBarEvent.OnPanic -> TODO()
            is TopAppBarEvent.OnSearch -> {filter(event.filter, event.everywhere)}
            TopAppBarEvent.DayClicked -> TODO()
            TopAppBarEvent.WeekClicked -> TODO()
            TopAppBarEvent.MedicinesClicked -> TODO()
            TopAppBarEvent.MonthClicked -> TODO()
            TopAppBarEvent.SettingsClicked -> TODO()
            TopAppBarEvent.ActionMenu -> TODO()
        }

    }
    fun postpone(item: Item){

    }
    private fun showAddItemDialog(){
        viewModelScope.launch {
            eventChannel.send(ChannelEvent.ShowAddItemDialog)
        }

    }
    private fun showMessage(message: String){
        viewModelScope.launch{
            eventChannel.send(ChannelEvent.ShowMessage(message))
        }
    }
    private fun showProgressBar(show: Boolean){
        viewModelScope.launch{
            eventChannel.send(ChannelEvent.ShowProgressBar(show))
        }
    }

    fun update(item: Item): Boolean {
        Logger.log("CalendarDateViewModel.update(Item)", item.heading)
        val rowsAffected = repository.update(item)
        if (rowsAffected != 1) {
            Logger.log("ERROR updating item")
            return false
        }
        return true
    }
}
