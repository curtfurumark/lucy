package se.curtrune.lucy.screens.item_editor

import se.curtrune.lucy.classes.Item

sealed interface ItemEditorEvent {
    data class Update(val item: Item): ItemEditorEvent
    data object StartTimer: ItemEditorEvent
    data object PauseTimer: ItemEditorEvent
    data object CancelTimer: ItemEditorEvent
    data object ResumeTimer: ItemEditorEvent
}