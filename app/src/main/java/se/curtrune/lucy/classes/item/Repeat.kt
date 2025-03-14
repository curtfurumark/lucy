package se.curtrune.lucy.classes.item

import java.time.DayOfWeek
import java.time.LocalDate

data class Repeat(
    var templateID: Long = 0,
    var qualifier: Int = 1,
    var unit: Unit = Unit.DAY,
    val weekDays: List<DayOfWeek> = emptyList(),
    var firstDate: LocalDate? = null,
    var lastDate: LocalDate? = null,
    var lastActualDate: LocalDate? = null,
    var isInfinite: Boolean = false
){
    enum class Unit{
        DAY, WEEK, MONTH, YEAR
    }
}