package se.curtrune.lucy.screens.item_editor

import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.screens.dev.DevEvent

sealed interface ItemEvent {
    data class Delete(val item: Item): ItemEvent
    data class Edit(val item: Item): ItemEvent
    data class GetChildren(val item: Item): ItemEvent
    data class GetChildrenType(val parent: Item, val type: Type): ItemEvent
    data class GetItem(val id: Long): ItemEvent
    data class InsertItem(val item: Item): ItemEvent
    data class Update(val item: Item): ItemEvent
    data object StartTimer: ItemEvent
    data object PauseTimer: ItemEvent
    data object CancelTimer: ItemEvent
    data object ResumeTimer: ItemEvent
    data object ShowAddItemDialog: ItemEvent
}