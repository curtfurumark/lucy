package se.curtrune.lucy.screens.medicine

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.MedicineContent
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.workers.ItemsWorker

class MedicineViewModel(
    val context: Context
) : ViewModel(){
    private val  _state = MutableStateFlow(MedicineState())
    val state = _state.asStateFlow()
    init {
        println("MedicineViewModel.init block")
        _state.update { it.copy(
            items = ItemsWorker.selectItems(Type.MEDICIN, context)
        ) }
    }
    fun addMedicine(medicine: MedicineContent){
        println("MedicineViewModel.addMedicine(medicine)")
        var item = Item()
        item.heading = medicine.name
        item.setType(Type.MEDICIN)
        item.content = medicine
        item = ItemsWorker.insert(item, context)
        println("item inserted with id ${item.id}");
    }
    fun deleteItem(item: Item){
        val deleted = ItemsWorker.delete(item, context)
        if( !deleted){
            _state.update { it.copy(
                errorMessage = "error deleting medicine"
            ) }
        }else{
            _state.update {
                it.copy(
                    items = ItemsWorker.selectItems(Type.MEDICIN, context)
                )
            }
        }
    }
    fun getMedicineList(): List<Item>{
        println("MedicineViewModel.getMedicineList()")
        return ItemsWorker.selectItems(Type.MEDICIN, context)
    }
    fun onEvent(event: MedicineEvent){
        println("...onEvent($event)")
        when(event){
            is MedicineEvent.Delete -> {
                deleteItem(event.item)
            }
            is MedicineEvent.Edit -> {
                println("edit item")
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
        }
    }
}

