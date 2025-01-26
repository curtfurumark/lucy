package se.curtrune.lucy.screens.duration

import se.curtrune.lucy.classes.Listable

data class DurationState(
    val period: DurationPeriod = DurationPeriod.DATE,
    //val items: List<Listable> = mutableListOf(),
    val items: List<Listable> = emptyList(),
    val showProgressBar: Boolean = false,
    val message: String = "",
    val totalDuration: Long = 0
)