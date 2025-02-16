package se.curtrune.lucy.screens.my_day

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.composables.Field
import se.curtrune.lucy.util.Logger
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
    private var _state = MutableStateFlow(MyDayState())
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
    fun onEvent(event: MyDateEvent){
        println("MentalDateViewModel.onEvent(MentalEvent)")
        when(event){
            is MyDateEvent.Date -> {
                setDate(event.date)
            }
            is MyDateEvent.SetEditField -> TODO()
            is MyDateEvent.UpdateItem -> {
                updateItem(event.item)
            }

            is MyDateEvent.AllDay -> { setAllDay(event.allDay)}
            is MyDateEvent.ShowDatePicker ->  {
                println("show date picker")
            }

            is MyDateEvent.Field -> { setField(event.field)}
        }

    }
    fun setDate(date: LocalDate) {
        Logger.log("MentalDateViewModel.setDate(LocalDate, Context)", date.toString())
        _state.update { it.copy(
            items = repository.selectItems(date),
            date = date
        ) }
    }
    private fun setField(field: Field){
        println("setField(${field.name})")
        _state.update { it.copy(
            currentField = field
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
