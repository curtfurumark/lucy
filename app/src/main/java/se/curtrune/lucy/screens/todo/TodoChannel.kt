package se.curtrune.lucy.screens.todo

import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.classes.item.Item

sealed interface TodoChannel {
    data class AddList(val parent: Item): TodoChannel
    data class Edit(val item: Item): TodoChannel
    data class Navigate(val navKey: NavKey): TodoChannel
    data object ShowAddItemDialog: TodoChannel
    data class ShowMessage(val message: String): TodoChannel
    data class ShowProgressBar(val show: Boolean): TodoChannel
}