package se.curtrune.lucy.screens.mental_stats

import se.curtrune.lucy.statistics.StatisticsPeriod

sealed interface MentalStatsEvent{
    data class Period(val period: StatisticsPeriod.Companion.Period): MentalStatsEvent
}