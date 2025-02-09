package se.curtrune.lucy.screens.dev

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.persist.LocalDB
import se.curtrune.lucy.screens.dev.test_cases.LocalDBTest
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.util.Logger

class DevActivityViewModel : ViewModel() {
    private var repository = LucindaApplication.repository
    private var _mentalModule = LucindaApplication.mentalModule
    private var _mental = MutableStateFlow(_mentalModule.current)
    var mental = _mental.asStateFlow()
    private var _state = MutableStateFlow(DevState())
    val state = _state.asStateFlow()

    init{
        println("....initBlock")
        _state.value.systemInfoList = LucindaApplication.systemInfoModule.systemInfo
        println("...number of sys infos: ${_state.value.systemInfoList.size}")
        _state.value.mental = LucindaApplication.mentalModule.current.value
    }

    private fun getChildren(parent: Item){
        println("...getChildren(parent: ${parent.heading})")
        val children = repository.selectChildren(parent)
        _state.update { it.copy(
            items = children
        ) }
    }
    private fun getItem(id: Long){
        println("...getItem(id: $id)")
        val item = repository.selectItem(id) ?: return
        println("got item: ${item.heading}")
        _state.update { it.copy(
            currentItem = item
        ) }

    }

    fun listColumns(context: Context?) {
        Logger.log("DevActivity.listColumns(Context)")
        LocalDB(context).use { db ->
            db.getColumns("items")
        }
    }
    fun onEvent(event: DevEvent){
        println("...onEvent(${event.toString()})")
        when(event){
            is DevEvent.CreateItemTree -> LocalDBTest().createTreeToDelete()
            is DevEvent.Search -> { search(event.query)}
        }
    }
    fun onEvent(event: ItemEvent){
        when(event){
            is ItemEvent.CancelTimer -> {}
            is ItemEvent.Delete -> {}
            is ItemEvent.PauseTimer -> {}
            is ItemEvent.ResumeTimer -> {}
            is ItemEvent.StartTimer -> {}
            is ItemEvent.Update ->  {update(event.item)}
            is ItemEvent.GetChildren -> {getChildren(event.item)}
            is ItemEvent.GetItem -> {getItem(event.id)}
        }


    }
    private fun search(filter: String){
        println("DevViewModel.search($filter)")
        val items = repository.search(filter)
        println("number items found ${items.size}")
    }
    private fun update(item: Item){
        println("update(${item.heading})")
        val rowsAffected = repository.update(item)
        if( rowsAffected != 1){
            println("error updating item")
        }
        _state.update { it.copy(
            items = it.items.filter { item-> item.type ==  Type.NODE }
        ) }
    }
    private fun filterList(){

    }
}
