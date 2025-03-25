package se.curtrune.lucy.screens.enchilada

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.top_app_bar.TopAppBarEvent
import se.curtrune.lucy.screens.ItemChannel
import se.curtrune.lucy.screens.item_editor.ItemEvent

class EnchiladaViewModel : ViewModel() {
    private val repository = LucindaApplication.repository
    private var items: List<Item> = mutableListOf()
    private val _state = MutableStateFlow(EnchiladaState())
    val state = _state.asStateFlow()
    private val eventChannel = Channel<ItemChannel>()
    val eventFlow = eventChannel.receiveAsFlow()


    init {
        items = repository.selectItems()
        //Logger.log("EnchiladaViewModel()")
        _state.update { it.copy(
            items = items
        ) }
    }

    fun onEvent(event: ItemEvent){
        when(event){
            is ItemEvent.CancelTimer -> {}
            is ItemEvent.Delete -> {delete(event.item)}
            is ItemEvent.GetChildren -> {}
            is ItemEvent.GetChildrenType -> {}
            is ItemEvent.GetItem -> {}
            is ItemEvent.PauseTimer -> {}
            is ItemEvent.ResumeTimer -> {}
            is ItemEvent.StartTimer -> {}
            is ItemEvent.Update -> {update(event.item)}
            is ItemEvent.Edit -> {edit(event.item)}
            is ItemEvent.InsertItem -> {insertItem(event.item)}
            is ItemEvent.ShowAddItemDialog -> {showAddItemDialog()}
            is ItemEvent.InsertChild -> {println("insert child")}
        }
    }
    fun onEvent(event: TopAppBarEvent){
        when(event){
            is TopAppBarEvent.DayCalendar -> {}
            is TopAppBarEvent.Menu -> {}
            is TopAppBarEvent.OnBoost -> {}
            is TopAppBarEvent.OnPanic -> {}
            is TopAppBarEvent.OnSearch -> {filter(event.filter)}
        }
    }

    private fun delete(item: Item): Boolean {
        println("EnchiladaViewModel.delete(Item)")
        val stat =repository.delete(item)
        return stat
    }
    private fun edit(item: Item){
        println("edit ${item.heading}")
        viewModelScope.launch {
            eventChannel.send(ItemChannel.Edit(item))
        }
    }
    private fun filter(filter: String){
        val filteredItems = items.filter { item-> item.contains(filter) }
        _state.update { it.copy(
            items = filteredItems
        ) }
    }
    private fun insertItem(item: Item){
        repository.insert(item)
        println("item inserted with id: ${item.id}")
    }
    private fun showAddItemDialog(){
        viewModelScope.launch {
            eventChannel.send(ItemChannel.ShowAddItemDialog)
        }
    }
    private fun showMessage(message: String){
        viewModelScope.launch {
            eventChannel.send(ItemChannel.ShowMessage(message))
        }
    }
    private fun update(item: Item){
        var rowsAffected = repository.update(item)
        if( rowsAffected != 1){
            showMessage("error updating item: ${item.heading}")
        }

    }
}
