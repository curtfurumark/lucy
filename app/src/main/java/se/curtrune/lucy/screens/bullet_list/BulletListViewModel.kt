package se.curtrune.lucy.screens.bullet_list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BulletListViewModel: ViewModel() {
    private val _state = MutableStateFlow(BulletListState())
    val state = _state.asStateFlow()


    fun onEvent(event: BulletListEvent){
        when(event){
            is BulletListEvent.OnClick -> TODO()
            is BulletListEvent.Update -> TODO()
        }

    }
}
