package se.curtrune.lucy.screens.todo

import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.classes.item.Item

sealed interface ChannelEvent {
    data class AddList(val parent: Item): ChannelEvent
    data class Edit(val item: Item): ChannelEvent
    data class Navigate(val navKey: NavKey): ChannelEvent
    data object ShowAddItemDialog: ChannelEvent
    data class ShowMessage(val message: String): ChannelEvent
    data class ShowProgressBar(val show: Boolean): ChannelEvent
}