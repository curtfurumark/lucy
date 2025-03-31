package se.curtrune.lucy.screens.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.modules.LucindaApplication

class ProjectsViewModel : ViewModel() {
    val repository = LucindaApplication.appModule.repository
    val _state = MutableStateFlow(ProjectsState())
    private val eventChannel = Channel<ProjectsChannel>()
    val eventFlow = eventChannel.receiveAsFlow()
    val state = _state.asStateFlow()
    @JvmField
    var currentParent: Item? = null
    private val stack: MutableList<Item> = ArrayList()

    init {
        currentParent = repository.getRootItem(Settings.Root.PROJECTS)
        println("ProjectsViewModel() init{}")
        val items = repository.selectChildren(currentParent)
        _state.update{
            it.copy(
                items = items,
                currentParent = currentParent)
        }
    }
    fun filter(filter: String?) {
        val filteredList: List<Item> = ArrayList()
        //filteredList = items
    }

    fun pop(): Item? {
        if (stack.size < 1) {
            return null
        }
        return stack.removeAt(stack.size - 1)
    }

    fun push(item: Item) {
        currentParent = item
        stack.add(item)
    }
    fun getStack(): List<Item> {
        return stack
    }
    fun onEvent(event: ProjectsEvent){
        when(event){
            is ProjectsEvent.Ascend -> TODO()
            is ProjectsEvent.Descend -> TODO()
            is ProjectsEvent.Delete -> TODO()
            is ProjectsEvent.OnItemClick -> { onItemClick(event.item) }
            is ProjectsEvent.UpdateItem -> {updateItem(event.item)}
            is ProjectsEvent.OnLongItemClick -> {}
            is ProjectsEvent.OnTabClick -> { onTabClick(event.tab) }
        }
    }

    private fun onTabClick(tab: String) {
        println("...onTabClick($tab)")

    }

    private fun onLongItemClick(item: Item) {
        println("...onLongItemClick(${item.heading})")
        viewModelScope.launch {
            eventChannel.send(ProjectsChannel.Edit(item))
        }
    }

    private fun updateItem(item: Item) {
        println("...updateItem(${item.heading})")
        val rowsAffected = repository.update(item)
        if( rowsAffected != 1){
            println("error updating item")
        }
    }

    private fun onItemClick(item: Item){
        println("...onItemClick(${item.heading})")
        if(item.hasChild()){
            stack.add(item)
            val items = repository.selectChildren(item)
            _state.update{
                it.copy(
                    items = items,
                    currentParent = item,
                    tabs = it.tabs + item.heading)
            }
            state.value.tabs.forEach{
                println("...$it")
            }
            //println("...$state.tabs")
        }else{
            println("...item does not have  child -> ItemEditorFragment")
            viewModelScope.launch {
                eventChannel.send(ProjectsChannel.Edit(item))
            }
        }
    }

    fun setRoot(root: Item) {
        this.currentParent = root
        currentParent = root
        stack.add(root)
    }
}
