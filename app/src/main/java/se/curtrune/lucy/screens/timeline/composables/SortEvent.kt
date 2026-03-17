package se.curtrune.lucy.screens.timeline.composables

sealed interface SortEvent {
    data object SortDateAscending : SortEvent
    data object SortDateDescending : SortEvent
    data object SortAlphabetically : SortEvent
    data object SortPriority : SortEvent

}