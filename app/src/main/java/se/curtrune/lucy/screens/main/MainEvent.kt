package se.curtrune.lucy.screens.main

import se.curtrune.lucy.classes.PanicOption
import se.curtrune.lucy.classes.item.Item

sealed interface MainEvent {
    data object CheckForUpdate: MainEvent
    data class ChosenPanicOption(val option: PanicOption): MainEvent
    data class ShowBoost(val show: Boolean): MainEvent
    data class ShowPanic(val show: Boolean): MainEvent
    data class StartSequence(val sequence: Item): MainEvent
}