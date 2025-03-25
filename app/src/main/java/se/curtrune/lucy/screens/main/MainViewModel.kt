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
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.app.Settings.PanicAction
import se.curtrune.lucy.app.UserPrefs
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.composables.top_app_bar.TopAppBarEvent
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.web.LucindaApi

class MainViewModel : ViewModel() {
    private val repository = LucindaApplication.repository
    private val mentalModule = LucindaApplication.mentalModule
    private val  _state =  MutableStateFlow(MainState())
    val state = _state.asStateFlow()
    private val _eventChannel = Channel<MainChannelEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()
    private val mutableMessage = MutableLiveData<String>()
    private val _filter = MutableStateFlow("")
    val filter = _filter.asStateFlow()
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
        Logger.log("LucindaViewModel.checkIfUpdateAvailable()")
        LucindaApplication.contextModule
        if( LucindaApplication.internetWorker.isConnected()){
            println("...internet is connected")
            viewModelScope.launch {
                val latestVersion = LucindaApi.create().getUpdateAvailable()
                val currentVersion = LucindaApplication.systemInfoModule.getVersionCode()
                if( currentVersion < latestVersion.versionCode){
                    println("update of lucinda available")
                    _state.update { it.copy(
                        versionInfo =  latestVersion
                    ) }
                    _eventChannel.send(MainChannelEvent.UpdateAvailable)
                }else{
                    println("...lucinda is up to date")
                }
            }
        }else{
            Logger.log("...internet is not connected")
        }

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
    fun onEvent(event: TopAppBarEvent){
        when(event){
            TopAppBarEvent.DayCalendar -> {}
            TopAppBarEvent.Menu -> {}
            TopAppBarEvent.OnBoost -> { requestAffirmation()}
            TopAppBarEvent.OnPanic -> {showPanicDialog()}
            is TopAppBarEvent.OnSearch -> {}
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


    fun filter(query: String) {
        Logger.log("MainViewModel.filter(query)", query)
        //mutableFilter.value = query
        _filter.update { query }
    }

    fun getPanicAction(context: Context?): PanicAction {
        Logger.log("LucindaViewModel.getPanicAction()")
        return UserPrefs.getPanicAction(context)
    }

    val message: LiveData<String>
        get() = mutableMessage

    private fun requestAffirmation() {
        println("LucindaViewModel.requestAffirmation()")
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
