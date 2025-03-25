package se.curtrune.lucy.screens.user_settings

sealed interface UserEvent {
    data class DarkMode(val darkMode: Boolean): UserEvent
    data class GetEvents(val calendarID: Int): UserEvent
    data class Language(val language: String): UserEvent
    data class SyncWithGoogle(val sync: Boolean): UserEvent
    data class SyncWithCalendar(val calendarID: Int): UserEvent
    data class GoogleCalendar(val id: Int): UserEvent
}