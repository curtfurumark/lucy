package se.curtrune.lucy.screens.duration

sealed interface DurationEvent {
    data class SetPeriod(val period: DurationPeriod) : DurationEvent
}
enum class DurationPeriod{
    DATE, WEEK, MONTH, YEAR
}