package se.curtrune.lucy.screens.lists

sealed interface ListEvent{
    data class SaveNote(val text: String) : ListEvent
    data object AddList: ListEvent
    data object CreateList : ListEvent
    data object CreateNote : ListEvent
}