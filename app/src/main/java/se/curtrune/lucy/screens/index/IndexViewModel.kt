package se.curtrune.lucy.screens.index

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class IndexViewModel: ViewModel(){
    private val _state = MutableStateFlow(IndexState())
    val state = _state.asStateFlow()


    fun onEvent(event: IndexEvent){
        when(event){
            is IndexEvent.Navigate -> {}
        }

    }
}