package se.curtrune.lucy.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.MutableStateFlow
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.classes.mental_fragment.MentalEvent
import se.curtrune.lucy.classes.mental_fragment.MentalState
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.workers.ItemsWorker
import java.time.LocalDate

class MentalDateViewModel(val context: Context) : ViewModel() {
    var mutableItems = MutableLiveData<List<Item>?>()
    private var _items: List<Item> = emptyList()
    var items: LiveData<List<Item>> = liveData {mutableItems  }
    var filteredItems: List<Item> = emptyList()
    var mutableAllDay = MutableLiveData<Boolean>(true)
    var currentStats = true
    var _state = MutableStateFlow(MentalState())
    init{
        println("init MentalDateViewModel(Context)")
        _items = ItemsWorker.selectItems(LocalDate.now(), context)
        //items.sort(Comparator.comparingLong { obj: Item -> obj.compareTargetTime() })
        mutableItems.value = _items
    }
    fun onEvent(event: MentalEvent){
        println("MentalDateViewModel.onEvent(MentalEvent)")
        when(event){
            is MentalEvent.SetDate -> {

            }
            is MentalEvent.SetEditField -> TODO()
            is MentalEvent.UpdateItem -> TODO()
        }

    }
    fun setDate(date: LocalDate, context: Context?) {
        Logger.log("MentalDateViewModel.setDate(LocalDate, Context)", date.toString())
        _items = ItemsWorker.selectItems(date, State.DONE, context)
        //items.sort(Comparator.comparingLong { obj: Item -> obj.compareTargetTime() })
        mutableItems.value = _items
    }
    fun updateItem(item: Item){
        println("....updateItem(Item)")
        ItemsWorker.update(item, context)
    }

    fun getItemsx(): LiveData<List<Item>?> {
        return mutableItems
    }

    fun setAllDay(allDay: Boolean) {
        println("...setAllDay($allDay)")
        if( allDay){
            mutableItems.value = _items
        }else{
            filteredItems = _items.filter { item->item.isDone }
            mutableItems.value = filteredItems
        }


    }
}
