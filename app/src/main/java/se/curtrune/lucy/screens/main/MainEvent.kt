package se.curtrune.lucy.screens.main

import se.curtrune.lucy.classes.item.Item

sealed interface MainEvent {
    data object CheckForUpdate: MainEvent
    data class ShowBoost(val show: Boolean): MainEvent
    data class ShowPanic(val show: Boolean): MainEvent
    data class StartSequence(val sequence: Item): MainEvent
}