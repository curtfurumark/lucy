package se.curtrune.lucy.screens.appoinment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.appoinment.AppointmentState
import se.curtrune.lucy.screens.appoinment.composables.AppointmentFabMenuEvent
import se.curtrune.lucy.screens.navigation.Route

class AppointmentViewModel(private val appointment: Item): ViewModel() {
    val _state = MutableStateFlow(AppointmentState(appointment))
    val state = _state.asStateFlow()
    val _channel = Channel<AppointmentChannel>()
    val channel = _channel.receiveAsFlow()
    val database = LucindaApplication.appModule.repository
    init {
        println("AppointmentViewModel init ${appointment.heading}")
        val children = database.selectChildren(appointment)
        _state.value = _state.value.copy(
            children = children
        )
    }

    fun onEvent(event: AppointmentFabMenuEvent) {
        when (event) {
            AppointmentFabMenuEvent.AddCheckableNote -> showAddChildDialog()
            AppointmentFabMenuEvent.AddContact -> addContact()
            AppointmentFabMenuEvent.AddToTimeLine -> addToTimeLine()
            AppointmentFabMenuEvent.AddVoiceRecording -> addVoiceRecording()
            AppointmentFabMenuEvent.AttachFile -> attachFile()
            AppointmentFabMenuEvent.Pending -> pending()
        }

    }
    fun onEvent(event: AppointmentEvent){
        when(event){
            is AppointmentEvent.Update -> {
                update(event.item)
            }
            AppointmentEvent.AddContact -> addContact()
            AppointmentEvent.AddDescription -> addDescription()
            AppointmentEvent.AddSummary -> addSummary()
            AppointmentEvent.AddVoiceRecording -> addVoiceRecording()
            AppointmentEvent.AttachFile -> attachFile()
            AppointmentEvent.Pending -> {}
            is AppointmentEvent.AddChild -> addChild(event.item)
            AppointmentEvent.AddCheckableNote -> addCheckableNote()
            AppointmentEvent.AddToTimeLine -> TODO()
        }
    }
    private fun addChild(item: Item){
        println("AppointmentViewModel addChild(${item.heading})")
        viewModelScope.launch {
            database.insertChild(parent = appointment, child = item)
            val children = database.selectChildren(appointment)
            _state.value = _state.value.copy(
                children = children
            )
        }
    }
    private fun addCheckableNote(){
        println("AppointmentViewModel addCheckableNote")
        message("add checkable note")
    }
    private fun addContact(){
        println("AppointmentViewModel addContact")
        message("add contact")
    }
    private fun addDescription(){
        println("AppointmentViewModel addDescription")
        viewModelScope.launch {
            _channel.send(AppointmentChannel.Message("add description"))
        }
    }
    private fun addSummary(){
        println("AppointmentViewModel addSummary")
        viewModelScope.launch {
            _channel.send(AppointmentChannel.Message("add summary"))
        }
    }
    private fun addToTimeLine(){
        println("AppointmentViewModel addToTimeLine")
        message("add to time line")
    }
    private fun addVoiceRecording(){
        println("AppointmentViewModel addVoiceRecording")
        viewModelScope.launch {
            _channel.send(AppointmentChannel.Navigate(Route.VoiceRecordingScreenNavKey))
        }
    }
    private fun attachFile(){
        println("AppointmentViewModel attachFile")
        message("attach file")
    }
    private fun message(message: String){
        viewModelScope.launch {
            _channel.send(AppointmentChannel.Message(message))
        }
    }
    private fun pending(){
        println("AppointmentViewModel pending")
        message("pending")
    }
    private fun showAddChildDialog(){
        viewModelScope.launch {
            _channel.send(AppointmentChannel.ShowAddChildDialog)
        }
    }
    private fun update(item: Item){
        println("AppointmentViewModel update ${item.heading}")
        val res = database.update(item)
        if( res != 1){
            println("error updating item")
        }else{
            println("item updated ok")
        }
    }
}