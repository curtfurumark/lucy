package se.curtrune.lucy.screens.create_list

sealed interface CreateListEvent {
    data class OnEnter(val text: String, val index: Int) : CreateListEvent
    data class OnDelete(val index: Int) : CreateListEvent
    data class OnUpdate(val index: Int, val text: String) : CreateListEvent

}