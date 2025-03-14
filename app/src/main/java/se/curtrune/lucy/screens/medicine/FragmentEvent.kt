package se.curtrune.lucy.screens.medicine

import se.curtrune.lucy.classes.item.Item

sealed interface FragmentEvent {
    data class NavigateToEditor(val item: Item): FragmentEvent
    data class ShowMessage(val message: String): FragmentEvent
}