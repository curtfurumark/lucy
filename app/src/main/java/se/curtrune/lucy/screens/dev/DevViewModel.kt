package se.curtrune.lucy.screens.dev

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.composables.top_app_bar.TopAppBarEvent
import se.curtrune.lucy.persist.SqliteLocalDB
import se.curtrune.lucy.screens.daycalendar.DayChannel
import se.curtrune.lucy.screens.dev.composables.MyTab
import se.curtrune.lucy.screens.dev.test_cases.LocalDBTest
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.util.Logger

class DevViewModel : ViewModel() {
    private var repository = LucindaApplication.repository
    private var _mentalModule = LucindaApplication.mentalModule
    private var _mental = MutableStateFlow(_mentalModule.current)
    var mental = _mental.asStateFlow()
    private val eventChannel = Channel<DevChannel>()
    val eventFlow = eventChannel.receiveAsFlow()
    private var _state = MutableStateFlow(DevState())
    val state = _state.asStateFlow()

    init{
        println("....initBlock")
        _state.value.systemInfoList = LucindaApplication.systemInfoModule.systemInfo
        println("...number of sys infos: ${_state.value.systemInfoList.size}")
        _state.value.mental = LucindaApplication.mentalModule.current.value
    }
    private fun dayCalendar(){
        println("day calendar")
        viewModelScope.launch {
            eventChannel.send(DevChannel.NavigateToDayCalendar)
        }
    }
    private fun addTab(heading: String){
        val newTab = MyTab.ItemTab(Item(heading))
        _state.update { it.copy(
            tabs = it.tabs + newTab
        ) }
    }

    private fun getChildren(parent: Item){
        println("...getChildren(parent: ${parent.heading})")
        val children = repository.selectChildren(parent)
        _state.update { it.copy(
            items = children
        ) }
    }
    private fun getChildrenType(parent: Item, type: Type){
        println("...getChildrenType(parent: ${parent.heading}, type: ${type.name})")
        val items = repository.selectTemplateChildren(parent)
        _state.update { it.copy(
            items = items
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
    private fun insertItemWithID(item: Item){
        println("...insertItemWithID(item ${item.heading})")
        repository.insert(item)
    }

    fun listColumns(context: Context?) {
        Logger.log("DevActivity.listColumns(Context)")
        SqliteLocalDB(context).use { db ->
            db.getColumns("items")
        }
    }
    private fun onBoost(){
        println("onBoost()")

    }
    fun onEvent(event: DevEvent){
        println("...onEvent(${event.toString()})")
        when(event){
            is DevEvent.CreateItemTree -> LocalDBTest().createTreeToDelete()
            is DevEvent.Search -> { search(event.query)}
            is DevEvent.ResetApp -> {resetLucinda()}
            is DevEvent.InsertItemWithID -> {insertItemWithID(event.item)}
            is DevEvent.AddTab -> {addTab(event.heading)}
            is DevEvent.RunQuery -> { runQuery(event.query)}
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
            is ItemEvent.GetChildrenType -> {getChildrenType(event.parent, event.type)}
            is ItemEvent.Edit -> {}
            is ItemEvent.InsertItem -> {}
            is ItemEvent.ShowAddItemDialog -> {}
            is ItemEvent.InsertChild -> {insertItem(event.item)}
        }
    }
    fun onEvent(event: TopAppBarEvent){
        when(event){
            is TopAppBarEvent.OnBoost -> { onBoost()}
            is TopAppBarEvent.OnPanic -> { onPanic()}
            is TopAppBarEvent.OnSearch -> {search(event.filter)}
            is TopAppBarEvent.DayCalendar -> {dayCalendar()}
            is TopAppBarEvent.Menu -> { showNavigationDrawer()}
        }
    }
    private fun insertItem(item: Item){
        println("insertItem(${item.heading})")
    }
    private fun onPanic(){
        println("onPanic()")

    }
    private fun resetLucinda(){
        println("resetLucinda()")
    }
    private fun runQuery(query: String){
        println("runQuery($query)")
        val db = LucindaApplication.localDB
        db.executeSQL(query)

    }
    private fun search(filter: String){
        println("DevViewModel.search($filter)")
        val items = repository.search(filter)
        println("number items found ${items.size}")
    }
    private fun showNavigationDrawer(){
        println("showNavigationDrawer()")
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
