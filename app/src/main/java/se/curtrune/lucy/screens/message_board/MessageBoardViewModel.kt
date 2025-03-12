package se.curtrune.lucy.screens.message_board

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.classes.Message
import se.curtrune.lucy.screens.message_board.composables.MessageBoardState
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.web.RetrofitInstance
import se.curtrune.lucy.web.RetrofitMessageInstance
import se.curtrune.lucy.workers.MessageWorker

class MessageBoardViewModel : ViewModel() {
    private val _state = MutableStateFlow(MessageBoardState())
    val state = _state.asStateFlow()

    private var currentCategory: String? = null
    private val mutableError = MutableLiveData<String>()
    private val mutableMessages = MutableLiveData<List<Message>?>()
    val errorMessage: LiveData<String>
        get() = mutableError

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

    fun insert(message: Message, context: Context?) {
        Logger.log("MessageBoardViewModel.insert(Message, Context)", message.subject)
/*        MessageWorker.insert(message) { result: DB1Result ->
            Logger.log("...onItemInserted(DB1Result)")
            if (result.isOK) {
                message.setID(result.id)
                //messages.add(0, message)
                mutableMessages.setValue(messages)
            } else {
                Logger.log("...error inserting message")
                Logger.log(result)
                mutableError.setValue("error inserting message")
            }
        }*/
    }

    fun filter(category: String?) {
/*        Logger.log("MessageBoardViewModel.filter(String)", category)
        val filteredMessages =
            messages!!.stream().filter { message: Message -> message.category == category }.collect(
                Collectors.toList()
            )
        mutableMessages.value = filteredMessages*/
    }
    fun onEvent(event: MessageBoardEvent){
        println("onEvent(${event.toString()})")
        when(event){
            is MessageBoardEvent.NewMessage -> TODO()
        }
    }

    fun update(message: Message, context: Context?) {
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
