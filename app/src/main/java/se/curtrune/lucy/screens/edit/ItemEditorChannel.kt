package se.curtrune.lucy.screens.edit

interface ItemEditorChannel {
    data object ShowAddChildDialog: ItemEditorChannel
    data class ShowMessage(val message: String): ItemEditorChannel
}