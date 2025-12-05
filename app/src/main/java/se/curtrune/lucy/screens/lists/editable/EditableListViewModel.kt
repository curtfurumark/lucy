package se.curtrune.lucy.screens.lists.editable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.lists.editable.EditableListEvent
import se.curtrune.lucy.screens.lists.editable.EditableListState

class EditableListViewModel: ViewModel() {
    private val db = LucindaApplication.Companion.appModule.repository
    private val _state = MutableStateFlow(EditableListState())
    val state = _state.asStateFlow()
    private val _channel = Channel<EditableListChannel>()
    val channel = _channel.receiveAsFlow()
    private var currentId: Long = 0
    init {
        _state.update {
            it.copy(
                item = Item(heading = "my list"),
                listItems = listOf(getItem(0)))
        }
    }
    private fun clearState() {
        _state.update {
            it.copy(
                item = Item(),
                listItems = emptyList(),
                focusIndex = 0
            )
        }
    }
    private fun getItem(id: Long): Item {
        println("...getItem $id")
        currentId++
        println("...currentId $currentId")
        return Item().apply {
            this.id = currentId    }
    }
    fun onEvent(event: EditableListEvent){
        when(event) {
            is EditableListEvent.AddItem -> {
                addItem(event.index)
            }

            EditableListEvent.SaveList -> {
                saveList()
            }

            EditableListEvent.Dismiss -> {
                dismiss()
            }

            is EditableListEvent.Update -> {
                update(event.item)
            }

            is EditableListEvent.UpdateListRoot -> {
                updateListRoot(event.root)
            }
        }
    }
    fun addItem(index: Int){
        println("...addItem $index")
        //currentId++
        _state.update {
            it.copy(
                focusIndex = index + 1,
                listItems = it.listItems.toMutableList().apply {
                    add(index+1,getItem(currentId))
                }
            )
        }
        println("...focusIndex ${_state.value.focusIndex}")
        for(item in _state.value.listItems) {
            println("...item ${item.heading}")
        }
    }
    private fun dismiss(){
        println("...dismiss")
        _state.update { it.copy(
            item = Item(),
            listItems = emptyList(),
            focusIndex = 0
        ) }
    }
    private fun message(message: String){
        println("...error $message")
        viewModelScope.launch {
            _channel.send(EditableListChannel.Message(message))
        }
    }
    private fun error(message: String){
        println("...error $message")
        viewModelScope.launch {
            _channel.send(EditableListChannel.Message(message))
        }
    }
    private fun saveList(){
        println("...saveList")
        val root = db.insert(_state.value.item)
        if( root == null){
            error("error inserting item ${_state.value.item.heading}")
            return
        }
        println("root inserted with id: ${root.id}")
        for(item in _state.value.listItems) {
            item.id = 0
            db.insertChild(root, item)
        }
        clearState()
    }
    private fun update(item: Item){
        println("...update ${item.heading}")
        val items = _state.value.listItems
        val index = items.indexOfFirst { it.id == item.id }
        if( index == -1){
            error("error updating item ${item.heading}")
            return
        }
        //items.find { it.id == event.item.id }?.heading = event.item.heading
        items[index].heading = item.heading
    }
    private fun updateListRoot(root: Item){
        println("...updateListRoot ${root.heading}")
        _state.update {
            it.copy(
                item = root
            )
        }
    }
}