package se.curtrune.lucy.screens.message_board

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.screens.medicine.MedicineChannelEvent
import se.curtrune.lucy.screens.message_board.composables.MessageBoardState
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.web.RetrofitInstance
import se.curtrune.lucy.workers.MessageWorker

class MessageBoardViewModel : ViewModel() {
    private val _state = MutableStateFlow(MessageBoardState())
    val state = _state.asStateFlow()
    private val mutableError = MutableLiveData<String>()
    private val eventChannel = Channel<MessageChannel>()
    val eventFlow = eventChannel.receiveAsFlow()
    init {
        println("init MessageBoardViewModel")
        getMessages()
    }
    private fun getMessages(){
        println("...getMessages()")
        viewModelScope.launch {
            val messages  = RetrofitInstance.messageApi.getMessages()
            _state.update { it.copy(
                messages = messages
            ) }
        }
    }

    private fun insert(message: Message) {
        println("MessageBoardViewModel.insert(${message.toString()})")

    }

    fun filter(category: String?) {
    }
    fun onEvent(event: MessageBoardEvent){
        println("onEvent(${event.toString()})")
        when(event){
            is MessageBoardEvent.NewMessage -> {
                insert(event.message)
            }
            is MessageBoardEvent.OnAddMessageClick -> {
                showAddMessageDialog()
            }
        }
    }
    private fun showAddMessageDialog(){
        viewModelScope.launch {
            eventChannel.send(MessageChannel.ShowAddMessageBottomSheet)
        }
    }

    fun update(message: Message) {
        Logger.log("...update(Message, Context)", message.subject)
        MessageWorker.update(message) { result ->
            Logger.log("...onUpdated(DBResult)", result.toString())
            if (!result.isOK) {
                Logger.log("...error updating message")
                mutableError.value = result.toString()
                Logger.log(result)
            } else {
                Logger.log("...update of message ok")
            }
        }
    }
}
