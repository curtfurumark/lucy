package se.curtrune.lucy.screens.todo

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
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.screens.appointments.UIEvent
import se.curtrune.lucy.util.Logger

class TodoViewModel : ViewModel() {
    private val _state = MutableStateFlow(TodoState())
    val state = _state.asStateFlow()
    private val repository = LucindaApplication.repository
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

 /*   fun filter(filter: String?) {
        val filteredItems: MutableList<Item> =
            items!!.stream().filter { item: Item -> item.contains(filter) }.collect(
            Collectors.toList()
        )
        mutableItems.value = filteredItems
    }*/

/*    fun getItem(index: Int): Item {
        return mutableItems.value!![index]
    }*/
    init {
        items = repository.selectItems(State.TODO).toMutableList()
        items.sortWith(compareByDescending { it.compare() })
        //items.sortedWith(compareByDescending { it.targetDate })
        _state.update { it.copy(
            items = items
        ) }
    }
    fun editItem(item: Item){
        viewModelScope.launch {
            eventChannel.send(ChannelEvent.Edit(item))
        }
    }

    fun insert(item: Item) {
        //var item = item
        println("TodoFragmentViewModel.insert(Item)")
        val itemWithID = repository.insert(item)
        //mutableItems.value!!.add(itemWithID)
        sort()
    }

    private fun sort() {
        //mutableItems.value!!.sortWith(Comparator.comparingLong { obj: Item -> obj.compare() })
    }
    fun onEvent(event: TodoEvent){
        when(event){
            is TodoEvent.Delete -> {delete(event.item)}
            is TodoEvent.Edit -> { editItem(event.item)}
            is TodoEvent.Insert -> {insert(event.item)}
            is TodoEvent.Update -> {update(event.item)}
            is TodoEvent.Postpone -> {postpone(event.item)}
        }
    }
    fun postpone(item: Item){

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
