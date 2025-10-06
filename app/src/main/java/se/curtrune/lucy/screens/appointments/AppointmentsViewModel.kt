package se.curtrune.lucy.screens.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.modules.TopAppbarModule
import se.curtrune.lucy.persist.Repository

class AppointmentsViewModel(private val repository: Repository) : ViewModel() {
    private val _state = MutableStateFlow(AppointmentsState())
    private val _eventChannel = Channel<AppointmentChannel>()
    private var items: List<Item> = mutableListOf()
    val eventChannel = _eventChannel.receiveAsFlow()
    val state = _state.asStateFlow()
    init {
        println("AppointmentsViewModel.init{}")
        items = repository.selectAppointments()
        _state.update {it.copy(
            items = items
            )
        }
        TopAppbarModule.setTitle("Möten")
    }
    private fun insert(item: Item) {
        println("AppointmentsViewModel(Item, Context)")
        val itemWithID = repository.insert(item)
        if( itemWithID == null){
            viewModelScope.launch {
                _eventChannel.send(AppointmentChannel.ShowMessage("error saving appointment"))
            }
            return
        }
        items = repository.selectAppointments()
        _state.update { it.copy(
                items = items
            )
        }
    }
    fun onEvent(event: AppointmentEvent){
        when(event){
            is AppointmentEvent.InsertAppointment ->{insert(event.item)}
            is AppointmentEvent.DeleteAppointment -> {delete(event.item)}
            is AppointmentEvent.Edit -> {editAppointment(event.item)}
            is AppointmentEvent.Update -> {update(event.item)}
            is AppointmentEvent.ShowAddAppointmentDialog -> { showAddAppointmentDialog() }
            is AppointmentEvent.Filter -> {filter(event.filter)}
            is AppointmentEvent.ShowDetails -> {showDetails(event.appointment)}
        }
    }

    /**
     * navigate to item editor
     */
    private fun editAppointment(appointment: Item){
        println("...editAppointment(${appointment.heading})")
        viewModelScope.launch{
            _eventChannel.send(AppointmentChannel.EditItem(appointment))
        }
    }

    fun filter(filter: String) {
        val filteredItems = items.filter { item->item.contains(filter) }
        _state.update { it.copy(
            items = filteredItems
        ) }
    }

    private fun delete(item: Item) {
        println("AppointmentsViewModel.delete(${item.heading})")
        val stat = repository.delete(item)
        if(!stat){
            viewModelScope.launch {
                _eventChannel.send(AppointmentChannel.ShowMessage("error deleting appointment"))
            }
            return
        }
        _state.update {
            it.copy(
                items = repository.selectItems((Type.APPOINTMENT))
            )
        }
    }

    private fun showAddAppointmentDialog(){
        _state.update {
            it.copy(
                defaultItemSettings = it.defaultItemSettings.copy(
                    item = Item().also {
                        item -> item.type = Type.APPOINTMENT
                        item.parent = repository.getAppointmentsRoot()
                        item.setIsCalenderItem(true)
                        item.setIsCalenderItem(true)
                        item.setIsAppointment(true)
                    },
                    parent = repository.getAppointmentsRoot()
                )
            )
        }
        viewModelScope.launch {
            _eventChannel.send(AppointmentChannel.ShowAddItemDialog)
        }
    }
    private fun showDetails(appointment: Item){
        println("AppointmentsViewModel.showDetails(${appointment.heading})")
        viewModelScope.launch {
            _eventChannel.send(AppointmentChannel.NavigateDetails(appointment))
        }
    }
    private fun update(item: Item) {
        println("AppointmentsViewModel.update(${item.heading})")
        val rowsAffected = repository.update(item)
        if (rowsAffected != 1) {
            viewModelScope.launch {
                _eventChannel.send(AppointmentChannel.ShowMessage("error updating appointment"))
            }
        }
    }
    class Factory(private val repository: Repository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AppointmentsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AppointmentsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
