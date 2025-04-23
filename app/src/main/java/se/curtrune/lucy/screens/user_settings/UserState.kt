package se.curtrune.lucy.screens.user_settings

import org.intellij.lang.annotations.Language
import se.curtrune.lucy.classes.google_calendar.GoogleCalendarEvent

data class UserState(
    var syncWithGoogle: Boolean = false,
    var googleCalendarID: Int = -1,
    val isDarkMode: Boolean = true,
    val usePassword: Boolean = false,
    val language: String = "sv",
    val calendarEvents: List<GoogleCalendarEvent> = emptyList(),
    var categories: List<String> = emptyList()
)
