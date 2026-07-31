package se.curtrune.lucy.screens.appoinment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.appoinment.AppointmentState
import se.curtrune.lucy.screens.appoinment.composables.AppointmentFabMenuEvent
import se.curtrune.lucy.screens.item_editor.ItemEditorViewModel
import se.curtrune.lucy.screens.navigation.Route
import java.nio.file.Files.delete

class AppointmentViewModel(private val appointment: Item): ViewModel() {
    val _state = MutableStateFlow(AppointmentState(appointment))
    val state = _state.asStateFlow()
    val _channel = Channel<AppointmentChannel>()
    val channel = _channel.receiveAsFlow()
    val database = LucindaApplication.appModule.repository
    init {
        println("AppointmentViewModel init ${appointment.heading}")
        refresh(appointment)
    }

    fun onEvent(event: AppointmentFabMenuEvent) {
        when (event) {
            AppointmentFabMenuEvent.AddCheckableNote -> showAddChildDialog()
            AppointmentFabMenuEvent.AddContact -> addContact()
            AppointmentFabMenuEvent.AddToTimeLine -> addToTimeLine()
            AppointmentFabMenuEvent.AddVoiceRecording -> addVoiceRecording()
            AppointmentFabMenuEvent.AttachFile -> attachFile()
            AppointmentFabMenuEvent.Pending -> pending()
            AppointmentFabMenuEvent.AddScript -> addScript()
            AppointmentFabMenuEvent.AddImage -> addImage()
        }

    }
    fun onEvent(event: AppointmentEvent){
        when(event){
            is AppointmentEvent.Update -> update(event.item)
            is AppointmentEvent.AddContact -> addContact()
            is AppointmentEvent.AddDescription -> addDescription()
            is AppointmentEvent.AddSummary -> addSummary()
            is AppointmentEvent.AddVoiceRecording -> addVoiceRecording()
            is AppointmentEvent.AttachFile -> attachFile()
            is AppointmentEvent.Pending -> {}
            is AppointmentEvent.AddChild -> addChild(event.item)
            is AppointmentEvent.AddCheckableNote -> addCheckableNote()
            is AppointmentEvent.AddToTimeLine -> addToTimeline()
            is AppointmentEvent.AddImage -> addImage()
            is AppointmentEvent.Refresh -> refresh(appointment)
            is AppointmentEvent.ViewFileItem -> viewFileItem(event.item)
            is AppointmentEvent.Delete -> delete(event.item)
        }
    }
    companion object {
        fun factory(item: Item): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AppointmentViewModel(item) as T
                }
            }
        }
    }

    private fun delete(item: Item) {
        println("AppointmentViewModel delete(${item.heading})")
        viewModelScope.launch {
            val stat =database.delete(item)
            println("stat: $stat")
        }
        refresh(appointment)
    }
    private fun viewFileItem(item: Item) {
        println("AppointmentViewModel viewFileItem(${item.heading})")
        viewModelScope.launch {
            _channel.send(AppointmentChannel.Navigate(Route.FileViewerScreenNavKey(item)))
        }
    }

    private fun refresh(parent: Item) {
        println("AppointmentViewModel refresh(${parent.heading})")
        val children = database.selectChildren(appointment)
        println("children: ${children.size}")
        children.forEach {
            println("child: ${it.heading}, ${it.parentId}}")
        }
        val media = children.filter { it.type == Type.MEDIA.ordinal }
        val checkList = children.filter { it.type != Type.MEDIA.ordinal }
        _state.value = _state.value.copy(
            children = checkList,
            media = media
        )
    }

    private fun addImage(){
        println("AppointmentViewModel addImage")
        viewModelScope.launch {
            _channel.send(AppointmentChannel.Navigate(Route.AttachImageScreenNavKey(appointment)))
        }
    }
    private fun addChild(item: Item){
        println("AppointmentViewModel addChild(${item.heading})")
        viewModelScope.launch {
            database.insertChild(parent = appointment, child = item)
        }
        refresh(appointment)
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
    private fun addToTimeline(){
        println("AppointmentViewModel addToTimeline")
        message("add to timeline")
    }
    private fun addScript(){
        println("AppointmentViewModel addScript")
        message("add script")
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
        //message("attach file")
        viewModelScope.launch {
            _channel.send(AppointmentChannel.Navigate(Route.AttachFileScreenNavKey(appointment)))
        }
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