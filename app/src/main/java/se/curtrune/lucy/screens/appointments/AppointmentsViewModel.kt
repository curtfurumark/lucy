package se.curtrune.lucy.screens.appointments

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.persist.ItemsWorker
import se.curtrune.lucy.util.Logger

class AppointmentsViewModel : ViewModel() {
    private val repository = LucindaApplication.repository
    private val _state = MutableStateFlow(AppointmentsState())
    val state = _state.asStateFlow()

    //private val mutableEvents = MutableLiveData<List<Item>?>()
    //private var items: MutableList<Item>? = null

    init {
        _state.update {it.copy(
            items = repository.selectItems(Type.APPOINTMENT).reversed()
        )
        }
    }
    private fun add(item: Item) {
        //var item = item
        Logger.log("AppointmentsViewModel(Item, Context)")
        val itemWithID = repository.insert(item) ?: return
        _state.update { it.copy(
            items = repository.selectItems(Type.APPOINTMENT)
        )
        }
    }
    fun onEvent(event: AppointmentEvent){
        when(event){
            is AppointmentEvent.AddAppointment ->{add(event.item)}
            is AppointmentEvent.DeleteAppointment -> {delete(event.item)}
            is AppointmentEvent.Edit -> {editAppointment(event.item)}
            is AppointmentEvent.Update -> {update(event.item)}
        }

    }
    private fun editAppointment(appointment: Item){
        println("...editAppointment(${appointment.heading})")
        _state.update {it.copy(
            editAppointment = true,
            currentAppointment = appointment
        )
        }
    }

    fun filter(filter: String?) {
/*        val filteredItems = items!!.stream().filter { item: Item -> item.contains(filter) }.collect(
            Collectors.toList()
        )*/
        //Collections.reverse();
        //mutableEvents.value = filteredItems
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
