package se.curtrune.lucy.screens.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.activities.kotlin.composables.DialogSettings
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.modules.MainModule
import se.curtrune.lucy.util.Logger

class AppointmentsViewModel : ViewModel() {
    private val repository = LucindaApplication.appModule.repository
    private val _state = MutableStateFlow(AppointmentsState())
    private val eventChannel = Channel<UIEvent>()
    private var items: List<Item> = mutableListOf()
    val eventFlow = eventChannel.receiveAsFlow()
    val state = _state.asStateFlow()
    val dialogSettings = DialogSettings()
    init {
        items = repository.selectAppointments()
        _state.update {it.copy(
            items = items
            )
        }
        MainModule.setTitle("möten")
    }
    private fun insert(item: Item) {
        println("AppointmentsViewModel(Item, Context)")
        val itemWithID = repository.insert(item) ?: return
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
            is AppointmentEvent.ShowAddAppointmentDialog -> {
                showAddAppointmentDialog()
            }

            is AppointmentEvent.Filter -> {filter(event.filter)}
        }
    }

    /**
     * navigate to item editor
     */
    private fun editAppointment(appointment: Item){
        println("...editAppointment(${appointment.heading})")
        viewModelScope.launch{
            eventChannel.send(UIEvent.EditItem(appointment))
        }
    }

    fun filter(filter: String) {
        val filteredItems = items.filter { item->item.contains(filter) }
        _state.update { it.copy(
            items = filteredItems
        ) }
    }

    private fun delete(item: Item) {
        Logger.log("AppointmentsViewModel.delete(Item, Context)", item.heading)
        val stat = repository.delete(item)
        if (stat) {
            _state.update { it.copy(
                items = repository.selectItems((Type.APPOINTMENT))
            ) }
        } else {
            println("ERROR deleting item: ${item.heading}")
        }
    }

    private fun showAddAppointmentDialog(){
        println("showAddAppointmentDialog()")
        dialogSettings.isCalendarItem = true
        dialogSettings.isAppointment = true
        dialogSettings.parent = repository.getAppointmentsRoot()
        viewModelScope.launch {
            eventChannel.send(UIEvent.ShowAddItemDialog)
        }
    }

/*    private fun sort() {
        Logger.log("...sort() not implemented")
        items!!.sortWith(Comparator.comparingLong { obj: Item -> obj.compare() }
            .reversed())
    }*/

    private fun update(item: Item) {
        println("AppointmentsViewModel.update(${item.heading})")
        val rowsAffected = repository.update(item)
        if (rowsAffected != 1) {
            println("ERROR updating item: ${item.heading}")
        }
    }
}
