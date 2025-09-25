package se.curtrune.lucy.screens.item_editor

import se.curtrune.lucy.classes.item.Item

interface ItemEditorChannel {
    data object ShowAddChildDialog: ItemEditorChannel
    data class ShowMessage(val message: String): ItemEditorChannel
}