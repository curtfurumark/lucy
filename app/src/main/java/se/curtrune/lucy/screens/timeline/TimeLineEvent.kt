package se.curtrune.lucy.screens.timeline

import se.curtrune.lucy.classes.item.Item

sealed interface TimeLineEvent {
    data class InsertItem(val item: Item): TimeLineEvent
    data class DeleteItem(val item: Item): TimeLineEvent
    data class OnClick(val item: Item): TimeLineEvent
}