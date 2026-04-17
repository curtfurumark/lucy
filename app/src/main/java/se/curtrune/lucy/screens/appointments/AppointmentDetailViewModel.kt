package se.curtrune.lucy.screens.appointments

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.app.LucindaApplication

class AppointmentDetailViewModel(private val appointmentID: Long): ViewModel() {
    private val _state = MutableStateFlow(AppointmentDetailState())
    val state = _state.asStateFlow()
    private val repository = LucindaApplication.appModule.repository
    init {
        println("AppointmentDetailViewModel.init($appointmentID)")
        val item = repository.selectItem(appointmentID)
        if( item == null){
            println("AppointmentDetailViewModel.init() error")
        }else {
            _state.update {
                it.copy(
                    item = item
                )
            }
        }
    }
}