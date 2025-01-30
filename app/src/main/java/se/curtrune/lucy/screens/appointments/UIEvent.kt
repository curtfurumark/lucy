package se.curtrune.lucy.screens.appointments

import se.curtrune.lucy.classes.Item

sealed interface UIEvent{
    data class EditItem(val item: Item): UIEvent
    data class ShowMessage(val message: String): UIEvent
}