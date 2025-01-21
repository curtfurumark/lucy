package se.curtrune.lucy.screens.duration

import se.curtrune.lucy.classes.Listable

data class DurationState(
    val period: DurationPeriod = DurationPeriod.DATE,
    var items: List<Listable> = mutableListOf()
)