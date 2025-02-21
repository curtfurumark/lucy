package se.curtrune.lucy.screens.dev

import se.curtrune.lucy.classes.Item

sealed interface DevEvent{
    data class AddTab(val heading: String): DevEvent
    data object CreateItemTree: DevEvent
    data class InsertItemWithID(val item: Item): DevEvent
    data object ResetApp: DevEvent
    data class Search(val query: String, val everywhere: Boolean): DevEvent
}