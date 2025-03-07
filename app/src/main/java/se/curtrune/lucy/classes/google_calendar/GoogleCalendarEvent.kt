package se.curtrune.lucy.classes.google_calendar

data class GoogleCalendarEvent(
    val calendarID: Int = -1,
    val eventID: Int = -1,
    val accountName: String,
    val title: String,
    val location: String,
    val isAllDay: Boolean,
    val dtStart: Long,
    val dtEnd: Long,
    val description: String,
    val timeZone: String = "Europe/Stockholm"
    )
