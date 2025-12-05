package se.curtrune.lucy.screens.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.screens.create_list.CreateListEvent

class ListViewModel: ViewModel() {
    private val repository = LucindaApplication.appModule.repository
    private val _state = MutableStateFlow(ListState())
    val state = _state.asStateFlow()
    private val _channel = Channel<ListChannel>()
    val channel = _channel.receiveAsFlow()
    var items: MutableList<String> = listOf<String>().toMutableList()


    init {
        items.add("item 0")
        _state.update { it.copy(
            //items = items
        ) }
    }
    fun setParent(parent: Item){
    }
    fun onEvent(event: ListEvent){
        when(event){
            is ListEvent.AddList -> {addList()}
            is ListEvent.CreateList -> {createList()}
            is ListEvent.CreateNote -> {createNote()}
            is ListEvent.SaveNote -> {saveNote(event.text)}
        }
    }

    fun onCreateListEvent(event: CreateListEvent){
        when(event) {
            is CreateListEvent.OnDelete -> {
                onDelete(event.index)
            }

            is CreateListEvent.OnEnter -> {
                onEnter(event.text, event.index)
            }

            is CreateListEvent.OnUpdate -> {
                onUpdate(event.index, event.text)
            }
        }
    }
    private fun onUpdate(index: Int, text: String) {
        println("...onUpdate(index: $index, text: $text)")
/*        if( text.endsWith("\n")){
            println("...text ends with new line $text")
            _state.update { it.copy(
                focusIndex = index + 1,
                items = items + ""
            ) }
        }else{
            items[index] = text
            println("...items: $items")
            _state.update {
                it.copy(
                    items = items,
                    focusIndex = index
                )
            }
        }*/
    }
    private fun onEnter(text: String, index: Int) {
        println("...onEnter(text: $text) index: $index")
/*        items[index] = text
        items.add(index + 1, "item: ${index  + 1}")
        println("...items size: ${items.size}")
        items.forEach{
            println("...item: $it")
        }
        _state.update { it.copy(
            items = items,
            focusIndex = index + 1
        ) }*/
    }

    private fun onDelete(index: Int) {
        println("onDelete(index: $index)")
/*        items.removeAt(index)
        _state.update { it.copy(
            items = items
        ) }*/
    }

    private fun createList() {
        println("creating list()")
        viewModelScope.launch {
            _channel.send(ListChannel.ShowMessage("creating list"))
        }
        _state.update { it.copy(
            listVisible = true,
            optionsVisible = false,
            noteVisible = false
        ) }
    }
    private fun createNote() {
        println("creating note")
        _state.update { it.copy(
            listVisible = false,
            optionsVisible = false,
            noteVisible = true
        ) }
        viewModelScope.launch {
            _channel.send(ListChannel.ShowMessage("creating note"))
        }
    }

    private fun saveNote(text: String) {
        println("saving note: $text")
        val item = Item("anteckning")
        item.comment = text
        item.setType(Type.NOTE)
        item.parent = repository.getTodoRoot()
        val itemWithID = repository.insert(item)
        if(itemWithID == null){
            println("error saving note")
            viewModelScope.launch {
                _channel.send(ListChannel.ShowMessage("error saving note: $text"))
            }
            return
        }
        viewModelScope.launch {
            _channel.send(ListChannel.ShowMessage("note saved with id: ${itemWithID.id}"))
        }
    }
    private fun addList() {
        println("adding list")
    }

}