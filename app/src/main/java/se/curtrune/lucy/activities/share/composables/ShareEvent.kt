package se.curtrune.lucy.activities.share.composables

import se.curtrune.lucy.classes.item.Item

sealed interface ShareEvent {
    data class Save(val item: Item) : ShareEvent
    data class OnLinkClicked(val url: String) : ShareEvent
}