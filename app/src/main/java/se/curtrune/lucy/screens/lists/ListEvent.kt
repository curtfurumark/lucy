package se.curtrune.lucy.screens.lists

sealed interface ListEvent{
    data object AddList: ListEvent
    data object CreateList : ListEvent
}