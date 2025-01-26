package se.curtrune.lucy.screens.mental

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.persist.ItemsWorker
import java.time.LocalDate

class MentalDateViewModel() : ViewModel() {
    private val mentalModule = LucindaApplication.mentalModule
    private val repository = LucindaApplication.repository
    private var allItems: List<Item> = mutableListOf()
    //var mutableItems = MutableLiveData<List<Item>?>()
    //private var _items: List<Item> = emptyList()
    //var items: LiveData<List<Item>> = liveData {mutableItems  }
    private var filteredItems: List<Item> = emptyList()
    //var mutableAllDay = MutableLiveData<Boolean>(true)
    private var _state = MutableStateFlow(MentalState())
    var state = _state.asStateFlow()
    init{
        println("init MentalDateViewModel(Context)")
        allItems = repository.selectItems(LocalDate.now())
        _state.update { it.copy(
            items = allItems
        ) }
        //items.sort(Comparator.comparingLong { obj: Item -> obj.compareTargetTime() })
        setAllDay(_state.value.allDay)
    }
    fun onEvent(event: MentalEvent){
        println("MentalDateViewModel.onEvent(MentalEvent)")
        when(event){
            is MentalEvent.SetDate -> {

            }
            is MentalEvent.SetEditField -> TODO()
            is MentalEvent.UpdateItem -> {
                updateItem(event.item)
            }

            is MentalEvent.AllDay -> { setAllDay(event.allDay)}
            is MentalEvent.ShowDatePicker ->  {
                println("show date picker")
            }
        }

    }
    fun setDate(date: LocalDate) {
        Logger.log("MentalDateViewModel.setDate(LocalDate, Context)", date.toString())
        _state.update { it.copy(
            items = repository.selectItems(date),
            date = date
        ) }
    }
    private fun updateItem(item: Item){
        println("....updateItem(Item)")
        repository.update(item)
        mentalModule.update()
    }

    private fun setAllDay(allDay: Boolean) {
        println("...setAllDay($allDay)")
        if( allDay){
            _state.update { it.copy(
                items = allItems
            ) }
        }else{
            _state.update {it.copy(
                items = allItems.filter { item->item.isDone }
                )
            }
        }
    }
}
