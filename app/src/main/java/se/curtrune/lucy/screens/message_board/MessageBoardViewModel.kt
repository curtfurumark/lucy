package se.curtrune.lucy.screens.message_board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.web.LucindaApi
import se.curtrune.lucy.workers.InternetWorker

class MessageBoardViewModel : ViewModel() {
    private val lucindaApi = LucindaApi.create()
    private val internetWorker = LucindaApplication.internetWorker
    private var messages :  MutableList<Message> = mutableListOf()
    private var filteredMessages: List<Message> = emptyList()
    private val _state = MutableStateFlow(MessageBoardState())
    val state = _state.asStateFlow()
    private val mutableError = MutableLiveData<String>()
    private val eventChannel = Channel<MessageChannel>()
    val eventFlow = eventChannel.receiveAsFlow()
    init {
        println("init MessageBoardViewModel")
        if(internetWorker.isConnected()){
            getMessages()
        }else{
            viewModelScope.launch {
                eventChannel.send(MessageChannel.ShowSnackBar("no internet connection"))
            }
        }
    }
    private fun getMessages(){
        println("...getMessages()")
        viewModelScope.launch {
            eventChannel.send(MessageChannel.ShowProgressBar(true))
            messages  = lucindaApi.getMessages().reversed().toMutableList()
            filteredMessages = messages.filter { message->message.category != "todo" }
            _state.update { it.copy(
                messages = filteredMessages
            ) }
            eventChannel.send(MessageChannel.ShowProgressBar(false))
        }
    }

    private fun insert(message: Message) {
        println("MessageBoardViewModel.insert(${message.toString()})")
        message.category = _state.value.category
        viewModelScope.launch {
            val strResult = lucindaApi.insertMessage(message)
            println("strResult: $strResult")
            messages.add(message)
            messages.sortBy { message->message.created }
            _state.update { it.copy(
                messages = messages.reversed()
            ) }
        }

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

            is MessageBoardEvent.SelectedCategory -> {
                setSelectedCategory(event.category)
            }

            is MessageBoardEvent.OnMessageClick -> {
                onMessageClick(event.message)
            }
        }
    }
    private fun onMessageClick(message: Message){
        println("onMessageClick")
        _state.update { it.copy(
            currentMessage = message
        ) }
        viewModelScope.launch {
            eventChannel.send(MessageChannel.ShowAddMessageBottomSheet)
        }
    }
    private fun setSelectedCategory(category: String){
        println("setSelectedCategory($category)")
        filteredMessages = messages.filter { message->message.category == category }
        _state.update { it.copy(
            category =  category,
            messages =  filteredMessages
        ) }
    }
    private fun showAddMessageDialog(){
        viewModelScope.launch {
            if( internetWorker.isConnected()) {
                eventChannel.send(MessageChannel.ShowAddMessageBottomSheet)
            }else{
                eventChannel.send(MessageChannel.ShowSnackBar("no internet connection"))
            }
        }
    }

    fun update(message: Message) {
        Logger.log("...update(Message, Context)", message.subject)
/*        MessageWorker.update(message) { result ->
            Logger.log("...onUpdated(DBResult)", result.toString())
            if (!result.isOK) {
                Logger.log("...error updating message")
                mutableError.value = result.toString()
                Logger.log(result)
            } else {
                Logger.log("...update of message ok")
            }
        }*/
    }
}
