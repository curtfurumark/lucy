package se.curtrune.lucy.activities.kotlin.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.MedicineContent
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.workers.ItemsWorker

class MedicineViewModel(
    val context: Context
) : ViewModel(){
    //val mutableMedicineList by mutableListOf<Item>()


    init {
        println("MedicineViewModel.init block")
        val items: List<Item> =  ItemsWorker.selectItems(Type.MEDICIN, context)
        println("number of items ${items.size}");
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
    fun getMedicineList(): List<Item>{
        println("MedicineViewModel.getMedicineList()")
        return ItemsWorker.selectItems(Type.MEDICIN, context)
    }
}

