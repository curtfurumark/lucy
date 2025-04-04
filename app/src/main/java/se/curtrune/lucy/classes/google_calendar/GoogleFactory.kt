package se.curtrune.lucy.classes.google_calendar

import se.curtrune.lucy.classes.item.Item
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

object GoogleFactory {


    fun googleEventToItem(googleEvent: GoogleCalendarEvent): Item {
        val item = Item(googleEvent.title)
        val startDateTime = millisEpochToLocalDateTime(googleEvent.dtStart)
        item.targetTime = startDateTime.toLocalTime()
        item.targetDate = startDateTime.toLocalDate()
        item.comment = googleEvent.description
        item.setIsCalenderItem(true)
        return item

    }
    fun itemToGoogleEvent(item: Item, calendarID: Int): GoogleCalendarEvent{
        val googleEvent = GoogleCalendarEvent(
            calendarID = calendarID,
            accountName = "",
            title = item.heading,
            dtStart = localDateTimeToMillisEpoch(item.targetDate, item.targetTime),
            dtEnd = 0L,
            location = "",
            isAllDay = false,
            eventID = 0,
            description = item.description,
            timeZone = ZoneId.systemDefault().toString(),
        )
        return googleEvent
    }
    private fun millisEpochToLocalDateTime(millis: Long): LocalDateTime{
        return  LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
    }
    private fun localDateTimeToMillisEpoch(date: LocalDate, time: LocalTime):Long{
        return  date.toEpochSecond(time, ZoneOffset.UTC) * 1000
    }
    private fun localDateTimeToMillisEpoch(dateTime: LocalDateTime): Long{
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
    }
}