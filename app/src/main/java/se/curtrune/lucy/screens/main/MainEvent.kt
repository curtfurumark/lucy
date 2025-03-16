package se.curtrune.lucy.screens.main

sealed interface MainEvent {
    data object CheckForUpdate: MainEvent
    data class ShowBoost(val show: Boolean): MainEvent
    data class ShowPanic(val show: Boolean): MainEvent
}