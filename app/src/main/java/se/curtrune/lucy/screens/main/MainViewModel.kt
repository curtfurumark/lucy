package se.curtrune.lucy.screens.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.app.Settings.PanicAction
import se.curtrune.lucy.app.UserPrefs
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.composables.top_app_bar.SearchFilter
import se.curtrune.lucy.composables.top_app_bar.TopAppBarEvent
import se.curtrune.lucy.screens.navigation.NavigationDrawerState
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.web.LucindaApi

class MainViewModel : ViewModel() {
    private val repository = LucindaApplication.appModule.repository
    private val mentalModule = LucindaApplication.appModule.mentalModule
    private val internetWorker = LucindaApplication.appModule.internetWorker
    private val userSettings = LucindaApplication.appModule.userSettings
    val navigationState = MutableStateFlow(NavigationDrawerState())
    private val  _state =  MutableStateFlow(MainState())
    val state = _state.asStateFlow()
    private val _topAppBarState = MutableStateFlow(TopAppBarState())
    val topAppBarState = _topAppBarState.asStateFlow()
    private val _eventChannel = Channel<MainChannelEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()
    private val mutableMessage = MutableLiveData<String>()
    private val _searchFilter = MutableStateFlow(SearchFilter("", false))
    val searchFilter = _searchFilter.asStateFlow()
    private val _mental = MutableLiveData<Mental>()
    var mental: LiveData<Mental> = _mental
    init {
        println("MainViewModel.init{}")
        _state.update { it.copy(
            mental =  mentalModule.current.value
        ) }
        checkIfUpdateAvailable()
    }


    private fun checkIfUpdateAvailable() {
        println("MainViewModel.checkIfUpdateAvailable()")
/*        if( checkInternet()){
            println("...internet is connected")
            viewModelScope.launch {
                try {
                    val latestVersion = LucindaApi.create().getUpdateAvailable()
                    val currentVersion =
                        LucindaApplication.appModule.systemInfoModule.getVersionCode()
                    if (currentVersion < latestVersion.versionCode) {
                        println("update of lucinda available")
                        _state.update {
                            it.copy(
                                versionInfo = latestVersion
                            )
                        }
                        _eventChannel.send(MainChannelEvent.UpdateAvailable)
                    } else {
                        println("...lucinda is up to date")
                    }
                }catch (e: Exception){
                    println("an exception occurred while checking for updates")
                    e.printStackTrace()
                    _eventChannel.send(MainChannelEvent.ShowMessage("an exception occurred while checking for updates"))
                }
            }
        }else{
            viewModelScope.launch {
                _eventChannel.send(MainChannelEvent.ShowMessage("no internet connection"))
            }
        }*/
    }
    fun onEvent(event: MainEvent){
        when(event){
            is MainEvent.ShowBoost -> {
                println("show boost")
                //requestAffirmation()
                requestQuote()
            }
            is MainEvent.ShowPanic -> {
                println("show panic")
                showPanicDialog()
            }

            MainEvent.CheckForUpdate -> {
                checkIfUpdateAvailable()
            }

            is MainEvent.StartSequence -> {
                startSequence()
            }
        }
    }
    fun navigate(fragment: LucindaFragment){
        println("MainViewModel.navigate(${fragment.toString()})")
        _state.update { it.copy(
            currentFragment = fragment
        ) }
        viewModelScope.launch {
            _eventChannel.send(MainChannelEvent.ShowNavigationDrawer(false))
        }
    }
    fun onEvent(event: TopAppBarEvent){
        println("MainViewModel.onEvent(${event})")
        when(event){
            is TopAppBarEvent.DayCalendar -> { showDayCalendar()}
            is TopAppBarEvent.DrawerMenu -> { openNavigationDrawer()}
            is TopAppBarEvent.OnBoost -> { requestAffirmation()}
            is TopAppBarEvent.OnPanic -> {showPanicDialog()}
            is TopAppBarEvent.OnSearch -> {filter(event.filter, event.everywhere)}
            is TopAppBarEvent.DayClicked -> { showDayCalendar()}
            is TopAppBarEvent.WeekClicked -> {}
            is TopAppBarEvent.MedicinesClicked -> TODO()
            is TopAppBarEvent.MonthClicked -> TODO()
            is TopAppBarEvent.SettingsClicked -> TODO()
            is TopAppBarEvent.ActionMenu -> {println("action menu")}
            is TopAppBarEvent.CheckForUpdate -> {checkIfUpdateAvailable()}
            is TopAppBarEvent.DevActivity -> {navigateDevActivity()}
        }
    }

    private fun navigateDevActivity() {
        println("MainViewModel.navigate()")
        viewModelScope.launch {
            _eventChannel.send(MainChannelEvent.ShowNavigationDrawer(false))
           _eventChannel.send(MainChannelEvent.NavigateDevActivity)
        }
    }

    private fun checkInternet(): Boolean{
        return internetWorker.isConnected()
    }
    private fun openNavigationDrawer(){
        Logger.log("LucindaViewModel.openNavigationDrawer()")
        viewModelScope.launch {
            _eventChannel.send(MainChannelEvent.ShowNavigationDrawer(true))
        }
    }
    fun setTitle(title: String){
        println("MainViewModel.setTitle($title)")
        _topAppBarState.update { it.copy(
            title = title
        ) }
    }
    private fun showDayCalendar(){
        println("MainViewModel.showDayCalendar()")
        viewModelScope.launch {
            _eventChannel.send(MainChannelEvent.ShowDayCalendar)
        }
    }
    private fun startSequence(){
        Logger.log("LucindaViewModel.startSequence()")
        val panicRoot = repository.getRootItem(Settings.Root.PANIC)
        if( panicRoot == null){
            Logger.log("panic root is null")
            return
        }
        viewModelScope.launch {
            _eventChannel.send(MainChannelEvent.StartSequence(panicRoot))
        }
    }


    fun filter(filter: String, everywhere: Boolean) {
        println("MainViewModel.filter($filter, everywhere $everywhere)")
        //mutableFilter.value = query
        //_filter.update { filter }
        _searchFilter.update { SearchFilter(filter, everywhere) }
    }

    fun getPanicAction(context: Context?): PanicAction {
        Logger.log("LucindaViewModel.getPanicAction()")
        return UserPrefs.getPanicAction(context)
    }

    val message: LiveData<String>
        get() = mutableMessage

    private fun requestAffirmation() {
        println("LucindaViewModel.requestAffirmation()")
        if( !checkInternet()){
            viewModelScope.launch {
                _eventChannel.send(MainChannelEvent.ShowMessage("no internet connection"))
            }
            println("...internet is not connected")
            return
        }
        viewModelScope.launch {
            val affirmation = LucindaApi.create().getAffirmation()
            println(affirmation.toString())
            _eventChannel.send(
                MainChannelEvent.ShowAffirmation(affirmation)
            )
        }
    }
    private fun requestQuote(){
        println("requestQuote()")
        viewModelScope.launch {
            val quotes = LucindaApi.create().getQuotes()
            val quote = quotes[0]
            _eventChannel.send(MainChannelEvent.ShowQuoteDialog(quote))
        }
    }

    private fun showPanicDialog(){
        viewModelScope.launch{
            _eventChannel.send(MainChannelEvent.ShowPanicDialog)
        }
    }

    private val currentFragment = MutableLiveData<Fragment>()

    fun updateFragment(fragment: Fragment) {
        Logger.log("LucindaViewModel.updateFragment(Fragment)")
        currentFragment.value = fragment
    }

    val fragment: LiveData<Fragment>
        get() = currentFragment



}
