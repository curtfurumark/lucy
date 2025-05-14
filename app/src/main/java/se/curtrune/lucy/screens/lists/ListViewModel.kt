package se.curtrune.lucy.screens.lists

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListViewModel: ViewModel() {
    private val _state = MutableStateFlow(ListState())
    val state = _state.asStateFlow()


    fun onEvent(event: ListEvent){
        when(event){
            is ListEvent.AddList -> {addList()}
            is ListEvent.CreateList -> {createList()}
        }

    }

    private fun createList() {
        TODO("Not yet implemented")
    }

    private fun addList() {
        TODO("Not yet implemented")
    }

}