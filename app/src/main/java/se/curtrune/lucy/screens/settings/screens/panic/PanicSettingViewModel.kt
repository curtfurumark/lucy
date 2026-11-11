package se.curtrune.lucy.screens.settings.screens.panic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.PanicOption
import se.curtrune.lucy.screens.daycalendar.DayCalendarState

class PanicSettingViewModel: ViewModel() {
    private val _state = MutableStateFlow(PanicSettingState())
    val state = _state.asStateFlow()
    private val _channel = Channel<PanicSettingChannel>()
    val channel = _channel.receiveAsFlow()

    private val settings = LucindaApplication.appModule.userSettings
    init {
        val panicOption = settings.panicOption
        _state.update { it.copy(
            panicOption = panicOption
        ) }
    }

    fun onEvent(event: PanicSettingEvent){
        when(event){
            is PanicSettingEvent.SetICE -> setICE(event.number)
            is PanicSettingEvent.SetBreathe -> setBreathe()
            is PanicSettingEvent.SetUrl -> setUrl(event.url)
            is PanicSettingEvent.SetUrlOption ->setUrlOption(event.checked)
            is PanicSettingEvent.SetPanicOption -> setPanicOption(event.option)
        }
    }
    private fun setBreathe(){
        println("set breathe")
        LucindaApplication.appModule.userSettings.panicOption = PanicOption.BREATHE
        _state.update { it.copy(
            panicOption = PanicOption.BREATHE
        ) }
    }
    private fun setPanicOption(option: PanicOption){
        println("set panic option")
        settings.panicOption = option
        _state.update { it.copy(
            panicOption = option
        ) }
    }
    private fun setUrl(url: String){
        println("set url")

    }
    private fun setUrlOption(checked: Boolean){
        println("set url")

    }
    private fun setICE(number: Int){
        println("set ICE")

    }
    private fun message(message: String){
        viewModelScope.launch {
            _channel.send(PanicSettingChannel.Message(message))
        }

    }
}