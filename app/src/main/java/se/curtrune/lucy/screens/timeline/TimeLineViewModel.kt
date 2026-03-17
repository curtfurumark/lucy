package se.curtrune.lucy.screens.timeline

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.navigation.ItemEditorNavKey
import se.curtrune.lucy.screens.timeline.composables.SortEvent
import se.curtrune.lucy.screens.top_appbar.TopAppbarModule

class TimeLineViewModel: ViewModel() {
    private val _channel = Channel<TimeLineChannel>()
    val channel = _channel.receiveAsFlow()

    private val _state = MutableStateFlow(TimeLineState())
    val state = _state.asStateFlow()
    private var items: List<Item> = emptyList()

    private val repository = LucindaApplication.appModule.repository
    init {
        TopAppbarModule.setTitle("tidslinje")
        TopAppbarModule.filterCallback = { filter ->
            filter(filter)
        }
        TopAppbarModule.searchScopeCallback = { everywhere ->
            setSearchScope(everywhere)

        }
        getItems()
    }


    fun onEvent(event: SortEvent){
        when(event) {
            SortEvent.SortAlphabetically -> sortAlphabetically()
            SortEvent.SortDateAscending -> sortDateAscending()
            SortEvent.SortDateDescending -> sortDateDescending()
            SortEvent.SortPriority -> sortPriority()
        }
    }


    fun onEvent(event: TimeLineEvent){
        when(event){
            is TimeLineEvent.InsertItem -> {
                insertItem(event.item)
            }

            is TimeLineEvent.DeleteItem -> {
                deleteItem(event.item)
            }
            is TimeLineEvent.OnClick -> {
                onClick(event.item)
            }

            is TimeLineEvent.EditItem -> TODO()
        }
    }

    private fun deleteItem(item: Item){
        repository.delete(item)
    }
    private fun filter(filter: String){
        println("filter($filter)")
        _state.value = _state.value.copy(
            items = items.filter { it.contains(filter) }
        )
    }
    private fun getItems(){
        items = repository.selectItems(Type.TIME_LINE)
        _state.value = _state.value.copy(
            items = items.sortedBy { it.targetDate }
        )
    }
    private fun insertItem(item: Item){
        item.setType(Type.TIME_LINE)
        repository.insert(item)
        getItems()
    }
    private fun onClick(item: Item){
        _channel.trySend(TimeLineChannel.Navigate(ItemEditorNavKey(item)))

    }
    private fun setSearchScope(everywhere: Boolean) {
        println("setSearchScope($everywhere)")
        if (everywhere) {
            items = repository.selectItems()
            println("items.size = ${items.size}")
            _state.value = _state.value.copy(
                items = items
            )
        } else {
            getItems()
        }
    }

    private fun sortAlphabetically() {
        println("...sortAlphabetically")
        _state.value = _state.value.copy(
            items = _state.value.items.sortedBy{ it -> it.heading}
        )
    }
    private fun sortDateAscending(){
        println("...sortDateAscending")
        _state.value = _state.value.copy(
            items = _state.value.items.sortedBy{ it -> it.targetDate}
        )
    }
    private fun sortDateDescending(){
        println("...sortDateDescending")
        _state.value = _state.value.copy(
            items = _state.value.items.sortedBy{ it -> it.targetDate}.reversed()
        )
    }
    private fun sortPriority() {
        println("...sortPriority()")
        _state.value = _state.value.copy(
            items = items.sortedBy { it.priority }.reversed()
        )
    }
}