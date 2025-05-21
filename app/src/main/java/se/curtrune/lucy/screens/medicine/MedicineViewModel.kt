package se.curtrune.lucy.screens.medicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.MedicineContent
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.modules.TopAppbarModule

class MedicineViewModel(
) : ViewModel(){
    private val repository = LucindaApplication.appModule.repository
    private val  _state = MutableStateFlow(MedicineState())
    val state = _state.asStateFlow()
    private val eventChannel = Channel<MedicineChannelEvent>()
    private var items: List<Item> = emptyList()
    val eventFlow = eventChannel.receiveAsFlow()
    init {
        println("MedicineViewModel.init block")
        items = repository.selectItems(Type.MEDICIN)
        _state.update { it.copy(
            items = items
        ) }
        TopAppbarModule.setTitle("Mediciner")
    }
    fun addMedicine(medicine: MedicineContent){
        println("MedicineViewModel.addMedicine(medicine)")
        var item = Item()
        item.heading = medicine.name
        item.setType(Type.MEDICIN)
        item.content = medicine
        item = repository.insert(item)!!
        println("item inserted with id ${item.id}");
    }
    private fun deleteItem(item: Item){
        val deleted = repository.delete(item)
        if( !deleted){
            _state.update { it.copy(
                errorMessage = "error deleting medicine"
            ) }
        }else{
            _state.update {
                it.copy(
                    items = repository.selectItems(Type.MEDICIN)
                )
            }
        }
    }
    private fun editItem(medicine: Item){
        viewModelScope.launch {
            eventChannel.send(MedicineChannelEvent.Edit(medicine))
        }
    }
    private fun filter(filter: String){
        val filteredItems = repository.selectItems(Type.MEDICIN).filter { item-> item.contains(filter) }
        _state.update { it.copy(
                items = filteredItems
            )
        }
    }

    fun onEvent(event: MedicineEvent){
        println("...onEvent($event)")
        when(event){
            is MedicineEvent.Delete -> {
                deleteItem(event.item)
            }
            is MedicineEvent.Edit -> {
                editItem(event.item)
            }
            is MedicineEvent.Sort -> {
                println(" sort items")
            }
            is MedicineEvent.ShowAddMedicineDialog -> {
                _state.update { it.copy(
                    showAddMedicineDialog =  event.show
                ) }
            }
            is MedicineEvent.Insert -> {
                println("....insert item ")
            }
            is MedicineEvent.ContextMenu -> {
                println("context menu: ${event.action}")
                viewModelScope.launch {
                    eventChannel.send(MedicineChannelEvent.ShowAdverseEffectsDialog)
                }
            }

            is MedicineEvent.Search -> {
                filter(event.filter)
            }
        }
    }
    private fun showAddMedicineDialog(action: String){
        when(action){
            "biverkning" -> {
                //showAdverseEffectDialog()
            }
        }
    }
}

