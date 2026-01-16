package se.curtrune.lucy.screens.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.InitialScreen
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.google_calendar.GoogleFactory
import se.curtrune.lucy.modules.TopAppbarModule

class SettingsViewModel: ViewModel(){
    private val calendarModule = LucindaApplication.appModule.googleCalendarModule
    private val eventChannel = Channel<UserChannel>()
    val eventFlow = eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(UserState())
    val state = _state.asStateFlow()
    private val userSettings = LucindaApplication.appModule.userSettings
    var categories = emptyList<String>()
    init {
        println("UserViewModel.init()")
        _state.update { it.copy(
            isDarkMode = userSettings.darkMode,
            syncWithGoogle = userSettings.syncWithGoogleCalendar,
            googleCalendarID = userSettings.googleCalendarId,
            language = userSettings.language,
            categories = userSettings.categories,
            usePassword = userSettings.usesPassword,
            password = userSettings.password,
            showMentalStatus = userSettings.showMentalStatus,
            mentalFlag = userSettings.mentalFlag,
            panicOption = userSettings.panicOption,
            initialScreen = userSettings.initialScreen,
            showProjects = userSettings.showProjects,
            showMedicine = userSettings.showMedicine,
            showDuration = userSettings.showDuration,
            showDevScreen = userSettings.showDevScreen,
            showMentalStats = userSettings.showMentalStatsScreen,
            isDevMode = userSettings.devMode
        ) }
        TopAppbarModule.setTitle("Inställningar")
    }
    private fun addCategory(category: String){
        userSettings.addCategory(category)
        _state.update { it.copy(
            categories = userSettings.categories
        ) }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getEvents(calendarID: Int){
        println("getEvents($calendarID)")
        val googleEvents = calendarModule.getEvents(calendarID)
        _state.update { it.copy(
            calendarEvents = googleEvents
        ) }
        googleEvents.forEach{ event->
            val item = GoogleFactory.googleEventToItem(event)
            println(item.toString())
        }

    }
    private fun googleCalendarId(id: Int){
        userSettings.googleCalendarId = id
        _state.update { it.copy(
            googleCalendarID = id
        ) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: UserEvent) {
        println("...onEvent($event)")
        when (event) {
            is UserEvent.DarkMode -> {
                setDarkMode(event.darkMode)
            }

            is UserEvent.SyncWithGoogle -> {
                syncWithGoogleCalendar(event.sync)
            }

            is UserEvent.GoogleCalendar -> {
                googleCalendarId(event.id)
            }

            is UserEvent.GetEvents -> {
                getEvents(event.calendarID)
            }

            is UserEvent.Language -> {
                setLanguage(event.language)
            }

            is UserEvent.SyncWithCalendar -> {
                syncWithCalendar(event.calendarID)
            }

            is UserEvent.ImportEvents -> {
                importEvents(event.googleCalendarID)
            }

            is UserEvent.SetPassword -> {
                setPassword(event.password)
            }
            is UserEvent.ShowTimeLine ->{
                println("show timeline")
                setShowTimeLine(event.visible)
            }
            is UserEvent.ShowToDo ->{
                println("show to do")
                setShowToDo(event.visible)
            }

            is UserEvent.DeleteCategory -> {
                deleteCategory(event.category)
            }

            is UserEvent.UsePassword -> {
                usePassword(event.usePassword)
            }

            is UserEvent.ShowMentalStatusChanged -> {
                setShowMentalStatus(event.showMentalStatus)
            }

            is UserEvent.AddCategory -> {
                addCategory(event.category)
            }

            is UserEvent.UpdateMentalFlag -> {
                updateMentalFlag(event.mentalFlag)
            }

            is UserEvent.SetPanicOption -> {setPanicOption(event.panicOption)}
            is UserEvent.SetInitialScreen -> {
                setInitialScreen(event.initialScreen)
            }

            is UserEvent.DevMode -> {
                println("dev mode")
            }
            is UserEvent.ShowProjects -> {
                println("show projects ${event.visible}")
                setShowProjects(event.visible)
            }

            is UserEvent.ShowMedicine -> {
                setShowMedicine(event.visible)
            }

            is UserEvent.ShowDuration -> {
                setShowDuration(event.visible)

            }

            is UserEvent.ShowDevScreen -> {
                setShowDevScreen(event.visible)

            }

            is UserEvent.ShowMentalStats -> {
                setShowMentalStats(event.visible)
            }
        }
    }

    private fun setShowMentalStats(visible: Boolean) {
        userSettings.showMentalStatsScreen = visible
    }

    private fun setShowDevScreen(show: Boolean) {
        userSettings.showDevScreen = show
    }
    private fun setShowDuration(show: Boolean) {
        userSettings.showDuration = show
    }

    private fun setShowMedicine(show: Boolean) {
        userSettings.showMedicine = show
    }
    private fun setShowTimeLine(show: Boolean)  {
        userSettings.showTimeLine = show

    }
    private fun setShowToDo(show: Boolean) {
        userSettings.showToDo = show
    }

    private fun setInitialScreen(initialScreen: InitialScreen){
        println("UserViewModel.setInitialScreen()")
        userSettings.initialScreen = initialScreen
    }
    private fun setPanicOption(panicOption: PanicOption){
        userSettings.panicOption = panicOption
        _state.update { it.copy(
            panicOption = panicOption
        ) }
    }
    private fun setShowMentalStatus(showMentalStatus: Boolean){
        TopAppbarModule.setShowMental(showMentalStatus)
        userSettings.showMentalStatus = showMentalStatus
        _state.update { it.copy(
            showMentalStatus = showMentalStatus
        ) }
    }

    private fun usePassword(usePassword: Boolean) {
        println("usePassword($usePassword)")
        userSettings.usesPassword = usePassword
        _state.update { it.copy(
            usePassword = usePassword
        ) }
    }
    private fun setShowProjects(visible: Boolean){
        println("setShowProjects($visible)")
        userSettings.showProjects = visible
        _state.update { it.copy(
            showProjects = visible
        ) }
    }

    private fun deleteCategory(category: String){
        println("deleteCategory($category)")
        userSettings.deleteCategory(category)
        _state.update { it.copy(
            categories = userSettings.categories
        ) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun importEvents(calendarID: Int) {
        println("importEvents($calendarID)")
        viewModelScope.launch {
            calendarModule.importGoogleEvents(calendarID)
        }

    }

    private fun setDarkMode(darkMode: Boolean){
        println("UserViewModel.setDarkMode($darkMode)")
        userSettings.darkMode = darkMode
    }
    private fun setPassword(pwd: String){
        println("UserViewModel.setPassword($pwd)")
        userSettings.password = pwd
    }
    private fun setLanguage(language: String){
        userSettings.language = language
    }

    private fun syncWithCalendar(calendarID: Int) {
        userSettings.googleCalendarId = calendarID
    }

    private fun syncWithGoogleCalendar(sync: Boolean){
        println("sync with google calendar($sync)")
        userSettings.syncWithGoogleCalendar = sync
        if( sync){
            viewModelScope.launch {
                eventChannel.send(UserChannel.ReadWriteCalendarPermissions)
            }
        }
    }
    private fun updateMentalFlag(mentalFlag: MentalFlag){
        userSettings.mentalFlag = mentalFlag
        _state.update { it.copy(
            mentalFlag = mentalFlag
        ) }
    }
}