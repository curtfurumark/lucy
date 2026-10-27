package se.curtrune.lucy.screens.settings.screens.main

import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.app.InitialScreen
import se.curtrune.lucy.screens.settings.screens.panic.PanicOption

sealed interface UserEvent {
    data class Navigate(val navKey: NavKey): UserEvent
    data class UsePassword(val usePassword: Boolean) : UserEvent
    data class DarkMode(val darkMode: Boolean): UserEvent
    data class GetEvents(val calendarID: Int): UserEvent
    data class Language(val language: String): UserEvent
    data class SyncWithGoogle(val sync: Boolean): UserEvent
    data class SyncWithCalendar(val calendarID: Int): UserEvent
    data class GoogleCalendar(val id: Int): UserEvent
    data class ImportEvents(val googleCalendarID: Int) : UserEvent
    data class SetPassword(val password: String) : UserEvent
    data class DeleteCategory(val category: String) : UserEvent
    data class ShowMentalStatusChanged(val showMentalStatus: Boolean) : UserEvent
    data class AddCategory(val category: String) : UserEvent
    data class UpdateMentalFlag(val mentalFlag: MentalFlag) : UserEvent
    data class SetInitialScreen(val initialScreen: InitialScreen) : UserEvent
    data class SetPanicOption(val panicOption: PanicOption) : UserEvent
    data class DevMode(val devMode: Boolean) : UserEvent

}