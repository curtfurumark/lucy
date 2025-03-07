package se.curtrune.lucy.classes

import java.time.DayOfWeek
import java.time.LocalDate

data class RepeatKt(
    val templateID: Int = -1,
    val unit: RepeatKt.Unit = Unit.SECONDS,
    val qualifier: Int,
    val infinite: Boolean,
    val firstDate: LocalDate,
    val currentLastDate: LocalDate?, //if infinite true, last date currently actual,
    val lastDate: LocalDate,
    val weekDays: List<DayOfWeek>
){
    enum class Unit{
        SECONDS, DAY, WEEK, MONTH, YEAR
    }
}
