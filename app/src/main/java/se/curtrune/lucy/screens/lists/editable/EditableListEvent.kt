package se.curtrune.lucy.screens.lists.editable

import se.curtrune.lucy.classes.item.Item

sealed interface EditableListEvent {
    data class AddItem(val index: Int): EditableListEvent
    data object Dismiss: EditableListEvent
    data object  SaveList: EditableListEvent
    data class Update(val item: Item): EditableListEvent
    data class UpdateListRoot(val root: Item): EditableListEvent

}