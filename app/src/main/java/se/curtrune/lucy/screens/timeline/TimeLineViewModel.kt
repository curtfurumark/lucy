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

class TimeLineViewModel: ViewModel() {
    private val _channel = Channel<TimeLineChannel>()
    val channel = _channel.receiveAsFlow()

    private val _state = MutableStateFlow(TimeLineState())
    val state = _state.asStateFlow()
    private val repository = LucindaApplication.appModule.repository
    init {
        getItems()
    }

    fun onEvent(event: TimeLineEvent){
        when(event){
            is TimeLineEvent.InsertItem -> {
                insertItem(event.item)
            }

            is TimeLineEvent.DeleteItem -> TODO()
            is TimeLineEvent.OnClick -> {
                onClick(event.item)
            }
        }
    }

    private fun deleteItem(item: Item){
        repository.delete(item)
    }
    private fun getItems(){
        val items = repository.selectItems(Type.TIME_LINE)
        _state.value = _state.value.copy(
            items = items
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

}