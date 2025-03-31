package se.curtrune.lucy.screens.user_settings

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
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.classes.google_calendar.GoogleFactory
import se.curtrune.lucy.modules.MainModule

class UserViewModel: ViewModel(){
    private val calendarModule = LucindaApplication.appModule.googleCalendarModule
    private val eventChannel = Channel<UserChannel>()
    val eventFlow = eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(UserState())
    val state = _state.asStateFlow()
    private val userSettings = LucindaApplication.appModule.userSettings
    var categories = emptyList<String>()
    init {
        _state.update { it.copy(
            isDarkMode = userSettings.darkMode,
            syncWithGoogle = userSettings.syncWithGoogleCalendar,
            googleCalendarID = userSettings.googleCalendarId,
            language = userSettings.language
        ) }
        MainModule.setTitle("inställningar")
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
    fun onEvent(event: UserEvent){
        println("...onEvent($event)")
        when(event){
            is UserEvent.DarkMode -> {setDarkMode(event.darkMode)}
            is UserEvent.SyncWithGoogle -> {syncWithGoogleCalendar(event.sync)}
            is UserEvent.GoogleCalendar -> {googleCalendarId(event.id)}
            is UserEvent.GetEvents -> { getEvents(event.calendarID)}
            is UserEvent.Language -> { setLanguage(event.language)}
            is UserEvent.SyncWithCalendar -> {syncWithCalendar(event.calendarID)}
        }

    }
    private fun setDarkMode(darkMode: Boolean){
        println("UserViewModel.setDarkMode($darkMode)")
        userSettings.darkMode = darkMode
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
/*    fun getLanguage(): String{
        return UserPrefs.getLanguage(context)
    }
    fun setLanguage(language: String){
        UserPrefs.setLanguage(language, context)
    }

    fun getPanicUrls(): Array<String> {
        return UserPrefs.getPanicUrls(context).toTypedArray()
    }

    fun isDarkMode(context: Context): Boolean {
        return UserPrefs.getDarkMode(context)
    }
    fun setDarkMode(darkMode: Boolean){
        UserPrefs.setUseDarkMode(darkMode, context)
    }*/

}