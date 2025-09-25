package se.curtrune.lucy.screens.duration

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.statistics.StatisticsPeriod
import java.time.LocalDate

enum class SortedBy{
    DATE, CATEGORY, TAGS
}

class DurationViewModel : ViewModel() {
    private var fromDate: LocalDate = LocalDate.now()
    private var toDate: LocalDate = LocalDate.now()
    private var stats = StatisticsPeriod(fromDate, toDate)
    private val _state = MutableStateFlow(DurationState())
    val state = _state

    init {
        _state.update {
            it.copy(
                totalDuration = stats.statistics.duration,
                items = stats.statistics.groupedByCategory,
                sortedBy = SortedBy.CATEGORY
            )
        }
    }
    fun onEvent(event: DurationEvent) {
        println("onEvent: ${event.javaClass.name}")
        when(event) {
            is DurationEvent.SetPeriod -> {setPeriod(event.period)}
            is DurationEvent.SetSortedBy -> {setSortedBy(event.sortedBy)}
            DurationEvent.Refresh -> {refresh()}
        }
    }
    private fun refresh() {
        println("refresh")
        stats = StatisticsPeriod(fromDate, toDate)
        _state.update{
            it.copy(
                totalDuration = stats.statistics.duration,
                items = when(it.sortedBy){
                    SortedBy.DATE -> stats.statistics.groupedByDate
                    SortedBy.CATEGORY -> stats.statistics.groupedByCategory
                    SortedBy.TAGS -> stats.statistics.groupedByTags
                }
            )
        }
    }

    private fun setSortedBy(sortedBy: SortedBy) {
        println("setSortedBy: ${sortedBy.name}")
        when(sortedBy){
            SortedBy.DATE -> {
                _state.update {
                    it.copy(
                        sortedBy = sortedBy,
                        items = stats.statistics.groupedByDate)
                }
            }

            SortedBy.CATEGORY -> {
                _state.update {
                    it.copy(
                        sortedBy = sortedBy,
                        items = stats.statistics.groupedByCategory)
                }
            }
            SortedBy.TAGS -> {
                _state.update {
                    it.copy(
                        sortedBy = sortedBy,
                        items = stats.statistics.groupedByTags)
                }
            }
        }
    }
    private fun setPeriod(period: StatisticsPeriod.Companion.Period) {
        println("viewModel setPeriod: ${period.name}")
        fromDate = when(period) {
            StatisticsPeriod.Companion.Period.DAY -> LocalDate.now()
            StatisticsPeriod.Companion.Period.WEEK -> LocalDate.now().minusDays(6)
            StatisticsPeriod.Companion.Period.MONTH -> LocalDate.now().minusMonths(1)
            StatisticsPeriod.Companion.Period.YEAR -> LocalDate.now().minusYears(1)
        }
        _state.update {
            println("update show progress bar")
            it.copy(
                showProgressBar = true,
            )
        }
        println("show progressbar: ${state.value.showProgressBar}")
        stats = StatisticsPeriod(fromDate, toDate)
        _state.update{
            it.copy(
                toDate = toDate,
                fromDate = fromDate,
                totalDuration = stats.statistics.duration,
                items = when(it.sortedBy){
                    SortedBy.DATE -> stats.statistics.groupedByDate
                    SortedBy.CATEGORY -> stats.statistics.groupedByCategory
                    SortedBy.TAGS -> stats.statistics.groupedByTags
                },
                showProgressBar = false
            )
        }
    }
}

