package se.curtrune.lucy.screens.log_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.screens.dev.DevChannel
import se.curtrune.lucy.screens.index.IndexState

class LogInViewModel: ViewModel() {
    private val _state = MutableStateFlow(LogInState())
    val state = _state.asStateFlow()
    private val userSettings = LucindaApplication.appModule.userSettings
    private val _eventChannel = Channel<LogInChannel>()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
        println("LogInViewModel.init")
        if( userSettings.usesPassword){
            println("uses password")
            _state.update { it.copy(
                usesPassWord =  true
            ) }
        }else {
            viewModelScope.launch {
                _eventChannel.send(LogInChannel.navigate(userSettings.initialScreen))
            }
        }
    }
    fun onEvent(event: LogInEvent){
        when(event){
            is LogInEvent.LogIn -> {checkPassWord(event.pwd)}
        }
    }

    private fun checkPassWord(pwd: String) {
        println("checkPassWord($pwd)")
        val passWord = userSettings.password
        if( pwd.trim() != passWord){
            println("error password")
            viewModelScope.launch {
                _eventChannel.send(LogInChannel.showMessage("errror password, try again"))
            }
        }else{
            viewModelScope.launch {
                _eventChannel.send(LogInChannel.navigate(userSettings.initialScreen))
            }
        }
    }
}