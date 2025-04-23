package se.curtrune.lucy.screens.duration

import se.curtrune.lucy.statistics.StatisticsPeriod

sealed interface DurationEvent {
    data class SetPeriod(val period: StatisticsPeriod.Companion.Period) : DurationEvent
    data class SetSortedBy(val sortedBy: SortedBy) : DurationEvent
}
enum class DurationPeriod{
    DATE, WEEK, MONTH, YEAR
}