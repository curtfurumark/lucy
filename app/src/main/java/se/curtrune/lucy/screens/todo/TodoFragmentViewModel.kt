package se.curtrune.lucy.screens.todo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.dialogs.PostponeDialog.Postpone
import se.curtrune.lucy.persist.ItemsWorker
import se.curtrune.lucy.util.Logger
import java.time.LocalDate
import java.util.stream.Collectors

class TodoFragmentViewModel : ViewModel() {
    private val _state = MutableStateFlow(TodoState())
    val state = _state.asStateFlow()
    private val repository = LucindaApplication.repository
    private var items: List<Item> = emptyList()
    //private val mutableItems = MutableLiveData<MutableList<Item>?>()


    fun delete(item: Item) {
        println("TodoFragmentViewModel(${item.heading})")
/*        var stat = repository.delete(item)
        if (!stat) {
            println("ERROR deleting item")
        } else {
            stat = mutableItems.value!!.remove(item)
            if (!stat) {
                println("ERROR removing item form mutableItems")
            }
        }*/
    }

 /*   fun filter(filter: String?) {
        val filteredItems: MutableList<Item> =
            items!!.stream().filter { item: Item -> item.contains(filter) }.collect(
            Collectors.toList()
        )
        mutableItems.value = filteredItems
    }*/

/*    fun getItem(index: Int): Item {
        return mutableItems.value!![index]
    }*/
    init {
        items = repository.selectItems(State.TODO)
        items.sortedWith(compareByDescending { it.targetDate })
        _state.update { it.copy(
            items = items
        ) }
    }



    fun insert(item: Item) {
        //var item = item
        println("TodoFragmentViewModel.insert(Item)")
        val itemWithID = repository.insert(item)
        //mutableItems.value!!.add(itemWithID)
        sort()
    }

    private fun sort() {
        //mutableItems.value!!.sortWith(Comparator.comparingLong { obj: Item -> obj.compare() })
    }
    fun onEvent(event: TodoEvent){
        when(event){
            is TodoEvent.Delete -> {delete(event.item)}
            is TodoEvent.Edit -> {}
            is TodoEvent.Insert -> {insert(event.item)}
            is TodoEvent.Update -> {update(event.item)}
        }
    }

    fun update(item: Item): Boolean {
        Logger.log("CalendarDateViewModel.update(Item)", item.heading)
        val rowsAffected = repository.update(item)
        if (rowsAffected != 1) {
            Logger.log("ERROR updating item")
            return false
        }
        return true
    }
}
