package se.curtrune.lucy.screens.todo

import se.curtrune.lucy.classes.item.Item

sealed interface ChannelEvent {
    data object ShowAddItemDialog: ChannelEvent
    data class ShowMessage(val message: String): ChannelEvent
    data class ShowProgressBar(val show: Boolean): ChannelEvent
    data class Edit(val item: Item): ChannelEvent
}