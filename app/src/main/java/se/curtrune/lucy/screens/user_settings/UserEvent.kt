package se.curtrune.lucy.screens.user_settings

sealed interface UserEvent {
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
    data class SetPanicOption(val panicOption: PanicOption) : UserEvent
}