package se.curtrune.lucy.screens.lists.editable

import androidx.navigation3.runtime.NavKey

sealed interface EditableListChannel {
    data class Message(val message: String): EditableListChannel
    data class Navigate(val navKey: NavKey): EditableListChannel
    data object NavigateBack: EditableListChannel
}