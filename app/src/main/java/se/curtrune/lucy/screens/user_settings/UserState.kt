package se.curtrune.lucy.screens.user_settings

import se.curtrune.lucy.classes.google_calendar.GoogleCalendarEvent

data class UserState(
    var syncWithGoogle: Boolean = false,
    var googleCalendarID: Int = -1,
    val isDarkMode: Boolean = true,
    val usePassword: Boolean = false,
    val password: String = "",
    val language: String = "sv",
    val calendarEvents: List<GoogleCalendarEvent> = emptyList(),
    var categories: List<String> = emptyList(),
    val showMentalStatus: Boolean = true,
    var mentalFlag: MentalFlag = MentalFlag(),
    var panicOption: PanicOption = PanicOption.URL
)

data class MentalFlag(
    var anxiety: Int = 0,
    var energy: Int = 0,
    var stress: Int = 0,
    var mood: Int = 0
)
