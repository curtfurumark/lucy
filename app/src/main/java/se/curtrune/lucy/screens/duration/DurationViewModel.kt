package se.curtrune.lucy.screens.duration

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class DurationViewModel : ViewModel() {
    private val _state = MutableStateFlow(DurationState())
    val state = _state


    fun onEvent(event: DurationEvent) {

    }
}

