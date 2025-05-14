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
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.screens.message_board.composables.DefaultMessage
import se.curtrune.lucy.screens.message_board.composables.Mode
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.web.LucindaApi
import java.time.LocalDateTime
import java.time.ZoneOffset

class MessageBoardViewModel : ViewModel() {
    private val lucindaApi = LucindaApi.create()
    private val internetWorker = LucindaApplication.appModule.internetWorker
    private var messages :  MutableList<Message> = mutableListOf()
    private var filteredMessages: List<Message> = emptyList()
    private val _state = MutableStateFlow(MessageBoardState())
    val state = _state.asStateFlow()
    private var currentCategory: String = "message"
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
            filteredMessages = messages.filter { message->message.category == currentCategory }.sortedByDescending { it.created }
            _state.update { it.copy(
                messages = filteredMessages,
                defaultMessage = DefaultMessage()
            ) }
        }
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

            is MessageBoardEvent.UpdateMessage -> {
                update(event.message)
            }

            is MessageBoardEvent.Search -> {
                println("search filter: ${event.filter}")
                search(event.filter, event.everywhere)
            }

            is MessageBoardEvent.OnAddMessageDismiss -> {
                _state.update { it.copy(
                    defaultMessage = DefaultMessage()
                ) }
            }
        }
    }
    private fun onMessageClick(message: Message){
        println("onMessageClick(${message.toString()})")
        _state.update { it.copy(
            defaultMessage = DefaultMessage(Mode.EDIT, message)
        ) }
        viewModelScope.launch {
            eventChannel.send(MessageChannel.ShowAddMessageBottomSheet)
        }
    }
    private fun search(filter: String, everywhere: Boolean){
        filteredMessages = messages.filter { message-> message.contains(filter) }
        _state.update {
            it.copy(
                messages = filteredMessages
            )
        }
    }
    private fun setSelectedCategory(category: String){
        println("setSelectedCategory($category)")
        currentCategory = category
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

    private fun update(message: Message) {
        println("...update(Message $message")
        _state.update { it.copy(
            defaultMessage = DefaultMessage()
        ) }
        message.updated = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        viewModelScope.launch {
            try {
                val strResult = lucindaApi.updateMessage(message)
                println("strResult: $strResult")
            }catch (exception: Exception){
                println(exception.message)
                eventChannel.send(MessageChannel.ShowSnackBar(exception.message.toString()))
            }
        }
    }
}
