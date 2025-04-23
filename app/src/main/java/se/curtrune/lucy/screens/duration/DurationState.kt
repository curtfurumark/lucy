package se.curtrune.lucy.screens.duration

import se.curtrune.lucy.classes.Listable
import se.curtrune.lucy.screens.duration.data.StatisticItem
import java.time.LocalDate

data class DurationState(
    val period: DurationPeriod = DurationPeriod.DATE,
    val sortedBy: SortedBy = SortedBy.CATEGORY,
    val items: List<StatisticItem> = emptyList(),
    val showProgressBar: Boolean = false,
    val message: String = "",
    val totalDuration: Long = 0,
    val fromDate: LocalDate = LocalDate.now(),
    val toDate: LocalDate = LocalDate.now()
)