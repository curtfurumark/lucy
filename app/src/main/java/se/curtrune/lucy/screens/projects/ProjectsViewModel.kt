package se.curtrune.lucy.screens.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.top_app_bar.TopAppBarEvent
import se.curtrune.lucy.persist.Repository
import java.time.LocalDate
import java.time.LocalTime

class ProjectsViewModel(private val repository: Repository) : ViewModel() {
    val _state = MutableStateFlow(ProjectsState())
    private val eventChannel = Channel<ProjectsChannel>()
    val eventFlow = eventChannel.receiveAsFlow()
    val state = _state.asStateFlow()
    var items: List<Item> = emptyList()
    @JvmField
    var currentParent: Item? = null
    private var numberTabs = 0
    init {
        println("ProjectsViewModel() init{}")
        currentParent = repository.getRootItem(Settings.Root.PROJECTS)
        items = repository.selectChildren(currentParent)
        _state.update{
            it.copy(
                items = items,
                currentParent = currentParent,
                tabs = it.tabs + ProjectTab(parent = currentParent, index = numberTabs++)
            )
        }
    }
    fun filter(filter: String) {
        val filteredItems = items.filter { item-> item.contains(filter) }
        _state.update {
            it.copy(
                items = filteredItems
            )
        }
    }

    fun onEvent(event: ProjectsEvent){
        when(event){
            is ProjectsEvent.Delete -> delete(event.item)
            is ProjectsEvent.ShowAddItemDialog -> {
                showAddItemDialog()
            }
            is ProjectsEvent.OnItemClick -> { onItemClick(event.item) }
            is ProjectsEvent.UpdateItem -> {updateItem(event.item)}
            is ProjectsEvent.OnLongItemClick -> { onLongItemClick(event.item)}
            is ProjectsEvent.OnTabClick -> { onTabClick(event.tab) }
            is ProjectsEvent.InsertItem -> {insertItem(event.item)}
        }
    }
    fun onEvent(event: TopAppBarEvent){
        when(event){
            is TopAppBarEvent.OnSearch -> {filter(event.filter)}
            TopAppBarEvent.ActionMenu -> TODO()
            TopAppBarEvent.CheckForUpdate -> TODO()
            TopAppBarEvent.DayCalendar -> TODO()
            TopAppBarEvent.DayClicked -> TODO()
            TopAppBarEvent.DevActivity -> TODO()
            TopAppBarEvent.DrawerMenu -> TODO()
            TopAppBarEvent.MedicinesClicked -> TODO()
            TopAppBarEvent.MonthClicked -> TODO()
            TopAppBarEvent.OnBoost -> TODO()
            TopAppBarEvent.OnPanic -> TODO()
            TopAppBarEvent.SettingsClicked -> TODO()
            TopAppBarEvent.WeekClicked -> TODO()
        }
    }
    private fun delete(item: Item) {
        println("...delete(${item.heading})")
        val deleted = repository.delete(item)
        if( !deleted){
            println("error deleting item")
            viewModelScope.launch {
                eventChannel.send(ProjectsChannel.ShowMessage("error deleting item"))
            }
        }
    }

    private fun insertItem(item: Item) {
        println("...insertItem(${item.heading})")
        val itemWithID = repository.insert(item)
        if (itemWithID == null) {
            println("error inserting item")
            viewModelScope.launch {
                eventChannel.send(ProjectsChannel.ShowMessage("error insert item"))
            }
            return
        }
        _state.update {
            it.copy(
                items = it.items + itemWithID,
            )
        }
    }

    private fun showAddItemDialog() {
        _state.update {it.copy(
            defaultItemSettings = it.defaultItemSettings.copy(
                parent = currentParent,
                isEvent = false,
                item = Item().also {
                    it.targetDate = LocalDate.of(0, 1, 1)
                    it.targetTime = LocalTime.ofSecondOfDay(0)
                    it.parent = currentParent
                }
            ))}
        viewModelScope.launch {
            eventChannel.send(ProjectsChannel.ShowAddItemDialog)
        }
    }

    private fun onTabClick(tab: ProjectTab) {
        println("...onTabClick($tab,   ${tab.index})")
        val tabs = state.value.tabs.filter { it.index < tab.index +1 }
        numberTabs = tabs.size
        items = repository.selectChildren(tab.parent)
        _state.update {
            it.copy(
                currentParent = tab.parent,
                items = items,
                tabs = tabs
            )
        }
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
            currentParent = item
            items = repository.selectChildren(item)
            _state.update{
                it.copy(
                    items = items,
                    currentParent = item,
                    tabs = it.tabs + ProjectTab(parent = item, index = numberTabs++))
            }
        }else{
            println("...item does not have  child -> ItemEditorFragment")
            viewModelScope.launch {
                eventChannel.send(ProjectsChannel.Edit(item))
            }
        }
    }

    class Factory(private val repository: Repository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProjectsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProjectsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
