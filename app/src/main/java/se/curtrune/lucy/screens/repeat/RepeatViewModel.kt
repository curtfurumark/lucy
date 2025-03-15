package se.curtrune.lucy.screens.repeat

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.item.Repeat

class RepeatViewModel: ViewModel() {
    private val repository = LucindaApplication.repository
    private val _state = MutableStateFlow(RepeatState())
    val state = _state.asStateFlow()
    var showDialog =  mutableStateOf(false)
    init {
        val repeats = repository.selectRepeats()
        _state.update { it.copy(
            repeats = repeats
        ) }
    }


    fun onRepeatClick(repeat: Repeat){
        showDialog.value = true
    }
}