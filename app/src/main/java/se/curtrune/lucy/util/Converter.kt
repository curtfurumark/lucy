package se.curtrune.lucy.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

object Converter {
    const val DATE_TIME_FORMAT_PATTERN: String = "yyyy-MM-dd HH:mm"
    const val DATE_FORMAT_PATTERN: String = "yyyy-MM-dd"
    const val DATE_FORMAT_SHORT_PATTERN: String = "dd/MM"
    const val TIME_FORMAT_PATTERN: String = "HH:mm"

    var VERBOSE: Boolean = false


    //2022-10-13
    fun epochTimeToFormattedString(time: Int): String? {
        return format(LocalTime.ofSecondOfDay(time.toLong()))
    }

    //2020-11-14 12:15:59
    fun epochToDate(epoch: Long): LocalDate {
        return LocalDate.ofEpochDay(epoch)
    }

    fun epochToFormattedDateTime(epoch: Long): String? {
        return formatUI(LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC))
    }


    fun format(date: LocalDate): String? {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN))
    }

    fun format(time: LocalTime): String? {
        return time.format(DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN))
    }

    /**
     * 
     * @param dateTime, the dateTime to be converted to formatted string representation
     * @return hours and minutes if same day, full format otherwise
     */
    fun formatUI(dateTime: LocalDateTime): String? {
        if (dateTime.toLocalDate() == LocalDate.now()) {
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
        return dateTime.format(DateTimeFormatter.ofPattern("E d/M - y"))
    }


    /**
     * 
     * @param secs number of seconds to format
     * @return secs formatted HH:mm:ss
     */
    fun formatSecondsWithHours(secs: Long): String {
        val hours = secs / 3600
        val minutes = (secs % 3600) / 60
        val seconds = secs % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }
}
