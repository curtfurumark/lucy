package se.curtrune.lucy.screens.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.activities.kotlin.composables.DialogSettings
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.composables.top_app_bar.TopAppBarEvent
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.screens.navigation.EditListNavKey
import se.curtrune.lucy.util.Logger
import java.util.Comparator

class TodoViewModel : ViewModel() {
    private val _state = MutableStateFlow(TodoState())
    val state = _state.asStateFlow()
    private val repository = LucindaApplication.appModule.repository
    private val topAppbarModule = TopAppbarModule
    private var items: MutableList<Item> = mutableListOf()
    private val _channel = Channel<ChannelEvent>()
    val channel = _channel.receiveAsFlow()

    fun delete(item: Item) {
        println("TodoViewModel.delete(${item.heading})")
        val stat = repository.delete(item)
        if (!stat) {
            println("ERROR deleting item")
            showMessage("error deleting item: ${item.heading}")
            return
        }
        showProgressBar(true)
        _state.update { it.copy(
            items =  repository.selectItems(State.TODO)
        ) }
        showProgressBar(false)
    }

    fun filter(filter: String, everywhere: Boolean) {
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
        TopAppbarModule.setTitle("todo/att göra")
        //val state = topAppbarModule.topAppBarState.collectAsState()
    }
    private fun editItem(item: Item){
        viewModelScope.launch {
            _channel.send(ChannelEvent.Edit(item))
        }
    }

    private fun insert(item: Item) {
        println("TodoViewModel.insert(Item)")
        val itemWithID = repository.insert(item)
        if( itemWithID == null){
            println("ERROR inserting item")
            showMessage("error inserting item: ${item.heading}")
            return
        }
        items.add(itemWithID)
        //TODO, sort items, is that necessary?
        items.sortWith(Comparator.comparingLong() { it.compare() })
        _state.update { it.copy(
            items = items
        ) }
    }
    private fun navigate(navKey: NavKey){
        viewModelScope.launch {
            _channel.send(ChannelEvent.Navigate(navKey))

        }
    }

    private fun sort() {
        //mutableItems.value!!.sortWith(Comparator.comparingLong { obj: Item -> obj.compare() })
    }
    fun onEvent(event: ItemEvent){
        when(event){
            is ItemEvent.AddChildren->{ addChildren(event.parent)}
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
            is ItemEvent.InsertItem -> { insert(event.item)}
            is ItemEvent.ShowAddItemDialog -> {showAddItemDialog()}
            is ItemEvent.InsertChild -> {
                println("insert child TODO")
            }

            is ItemEvent.AddCategory -> TODO()
            is ItemEvent.ShowChildren -> {
                showChildren(event.parent)
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
            TopAppBarEvent.CheckForUpdate -> TODO()
            TopAppBarEvent.DevActivity -> TODO()
        }

    }
    private fun addChildren(parent: Item){
        println("TodoViewModel.addChildren(Item)")
        viewModelScope.launch {
            _channel.send(ChannelEvent.Navigate(EditListNavKey(parent)))
        }
    }
    fun postpone(item: Item){

    }
    private fun showAddItemDialog(){
        val parent = repository.getTodoRoot()
        _state.update { it.copy(
            newItemSettings = DialogSettings(isCalendarItem = false, parent = parent)
        ) }
        viewModelScope.launch {
            _channel.send(ChannelEvent.ShowAddItemDialog)
        }

    }
    private fun showMessage(message: String){
        viewModelScope.launch{
            _channel.send(ChannelEvent.ShowMessage(message))
        }
    }
    private fun showProgressBar(show: Boolean){
        viewModelScope.launch{
            _channel.send(ChannelEvent.ShowProgressBar(show))
        }
    }
    private fun showChildren(parent: Item) {
        showMessage("show children not implemented")
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
