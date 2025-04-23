package se.curtrune.lucy.screens.create_list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.classes.item.Item

class CreateListViewModel: ViewModel(){
    private var _state = MutableStateFlow(CreateListState())
    var state = _state.asStateFlow()
    var items: MutableList<String> = listOf<String>().toMutableList()
    //var items: List<String>  = emptyList()
    init {
        println("...init CreateListViewModel")
        items.add("item 0")
        _state.update { it.copy(
            items = items
        ) }
    }
    fun setParent(parent: Item){
        _state.update { it.copy(
            parent = parent
        ) }
    }

    fun onEvent(event: CreateListEvent){
        when(event){
            is CreateListEvent.OnDelete -> { onDelete(event.index)}
            is CreateListEvent.OnEnter -> {onEnter(event.text, event.index)}
            is CreateListEvent.OnUpdate -> {onUpdate(event.index, event.text)}}
        }

    private fun onUpdate(index: Int, text: String) {
        println("...onUpdate(index: $index, text: $text)")
        if( text.endsWith("\n")){
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

        }
    }
    private fun onEnter(text: String, index: Int) {
        println("...onEnter(text: $text) index: $index")
        items[index] = text
        items.add(index + 1, "item: ${index  + 1}")
        println("...items size: ${items.size}")
        items.forEach{
            println("...item: $it")
        }
        _state.update { it.copy(
            items = items,
            focusIndex = index + 1
        ) }
    }

    private fun onDelete(index: Int) {
        println("onDelete(index: $index)")
        items.removeAt(index)
        _state.update { it.copy(
            items = items
        ) }
    }
}

