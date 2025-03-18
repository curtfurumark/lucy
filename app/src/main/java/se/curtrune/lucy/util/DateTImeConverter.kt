package se.curtrune.lucy.util

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object DateTImeConverter {

    fun epochSecondToFormattedDateTimeString(epochSeconds: Long): String{
        return format( LocalDateTime.ofEpochSecond(epochSeconds, 0, ZoneOffset.UTC))

    }
    fun format(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern(Converter.DATE_FORMAT_PATTERN))
    }
    fun format(dateTime: LocalDateTime?): String {
        if (dateTime == null) {
            return ""
        }
        return dateTime.format(DateTimeFormatter.ofPattern(Converter.DATE_TIME_FORMAT_PATTERN))
    }

    fun format(time: LocalTime): String {
        return time.format(DateTimeFormatter.ofPattern(Converter.TIME_FORMAT_PATTERN))
    }
    /**
     *
     * @param secs number of seconds to format
     * @return secs formatted HH:mm:ss
     */
    fun formatSeconds(secs: Long): String {
        val days = secs / (24 * 3600)
        println("days: $days")
        println("seconds remaining after days: ${secs % (24 * 3600)}")
        val hours = secs % (24 * 3600) / 3600 //modden 3600 + 60 + 42  = 3702
        val minutes = (secs % 3600) / 60
        val seconds = secs % 60
        val stringDays = if( days > 0 ) "$days day${if (days > 1) "s " else " "}" else ""
        return String.format(Locale.getDefault(), "%s%02d:%02d:%02d", stringDays, hours, minutes, seconds)
    }

    fun format(yearMonth: YearMonth): String{
        return String.format(Locale.getDefault(), "%s", yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()))
    }

    fun format(dayOfWeek: DayOfWeek, textStyle: TextStyle): String{
        //return String.format(Locale.getDefault(), "%s %d",dayOfWeek.getDisplayName(textStyle, Locale.getDefault()).cecilia())
        return dayOfWeek.getDisplayName(textStyle, Locale.getDefault()).cecilia()
    }
    fun epochMillisToLocalDate(epochMillis: Long): LocalDate{
        return  LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault()).toLocalDate()

    }
    fun epochMillisToLocalTime(epochMillis: Long): LocalTime{
        return  LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault()).toLocalTime()

    }
}