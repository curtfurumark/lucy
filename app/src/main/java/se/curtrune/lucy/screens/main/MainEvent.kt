package se.curtrune.lucy.screens.main

sealed interface MainEvent {
    data class ShowBoost(val show: Boolean): MainEvent
    data class ShowPanic(val show: Boolean): MainEvent
}