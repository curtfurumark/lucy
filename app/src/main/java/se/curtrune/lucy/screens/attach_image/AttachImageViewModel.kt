package se.curtrune.lucy.screens.attach_image

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import se.curtrune.lucy.classes.item.Item

class AttachImageViewModel(private val item: Item): ViewModel() {
    private val _state = MutableStateFlow(AttachImageState(item))
    val state = _state.asStateFlow()
    init {
        println("AttachImageViewModel(item)")
    }

    fun onEvent(event: AttachImageEvent){
        when(event){
            is AttachImageEvent.OnImageFailure -> onImageFailure()
            is AttachImageEvent.OnImageSuccess -> onImageSuccess()
        }
    }
    private fun onImageFailure(){

    }
    private fun onImageSuccess(){

    }
}