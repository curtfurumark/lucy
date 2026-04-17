package se.curtrune.lucy.screens.my_manual

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.my_manual.form.DataOne

class MyManualGuideViewmodel : ViewModel() {
    private val repository = LucindaApplication.appModule.repository
    private val _state = MutableStateFlow(MyManualGuideState())
    val state = _state.asStateFlow()

    init {
        println("MyManualGuideViewmodel().init{}")
        _state.value = _state.value.copy(
            medicines = repository.selectItems(Type.MEDICIN)
        )
    }
    fun onEvent(event: MyManualGuideEvent){
        when(event){
            is MyManualGuideEvent.OnPageFourCompleted -> onPageFourCompleted()
            is MyManualGuideEvent.OnPageOneCompleted -> onPageOneCompleted(event.data)
            is MyManualGuideEvent.OnPageThreeCompleted -> onPageThreeCompleted()
            is MyManualGuideEvent.OnPageTwoCompleted -> onPageTwoCompleted()
            is MyManualGuideEvent.OnAddMedicine -> {onAddMedicine(event.medicine)}
            is MyManualGuideEvent.OnAddTrigger -> onAddTrigger(event.trigger)
            is MyManualGuideEvent.OnAddSensitivity -> TODO()
            is MyManualGuideEvent.OnBackPressed -> { onBackPressed()}
            is MyManualGuideEvent.OnNameChanged -> onNameChanged(event.name)

        }
    }
    private fun onAddMedicine(medicine: Item){
        println("...onAddMedicine ${medicine.heading}")
        medicine.setType(Type.MEDICIN)
        repository.insert(medicine)
        _state.value = _state.value.copy(
            medicines = repository.selectItems(Type.MEDICIN)
        )
    }
    private fun onAddTrigger(trigger: Item){
        println("...onAddTrigger")
    }
    private fun onBackPressed(){
        println("...onBackPressed")
        _state.value = _state.value.copy(
            currentPage = _state.value.currentPage - 1
        )

    }
    private fun onNameChanged(name: String) {
        println("...onNameChanged")
        _state.value = _state.value.copy(
            name = name
        )
    }

    private fun onPageOneCompleted(data: DataOne){
        println("...onPageOneCompleted")
        _state.value = _state.value.copy(
            currentPage = 1
        )
    }
    private fun onPageTwoCompleted(){
        println("...onPageTwoCompleted")
        _state.value = _state.value.copy(
            currentPage = 2
        )
    }
    private fun onPageThreeCompleted(){
        println("...onPageThreeCompleted")
        _state.value = _state.value.copy(
            currentPage = 3
        )
    }
    private fun onPageFourCompleted(){
        println("...onPageFourCompleted")
        _state.value = _state.value.copy(
            currentPage = 0
        )

    }


}