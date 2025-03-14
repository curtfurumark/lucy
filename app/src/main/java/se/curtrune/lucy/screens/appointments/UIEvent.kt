package se.curtrune.lucy.screens.appointments

import se.curtrune.lucy.classes.item.Item

sealed interface UIEvent{
    data class EditItem(val item: Item): UIEvent
    data object ShowAddItemDialog: UIEvent
    data class ShowMessage(val message: String): UIEvent
}