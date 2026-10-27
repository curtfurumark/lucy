package se.curtrune.lucy.screens.settings.screens.panic

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PanicSettingViewModel: ViewModel() {
    val _state = MutableStateFlow(PanicSettingState())
    val state = _state.asStateFlow()


    fun onEvent(event: PanicSettingEvent){
        when(event){
            is PanicSettingEvent.SetICE -> setICE(event.number)
            is PanicSettingEvent.SetBreathe -> setBreathe(event.breath)
        }
    }
    private fun setBreathe(breathe: Boolean){
        println("set breathe")
    }
    private fun setICE(number: Int){
        println("set ICE")

    }
    private fun message(){

    }
}