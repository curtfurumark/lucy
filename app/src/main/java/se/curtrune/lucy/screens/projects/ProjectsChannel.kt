package se.curtrune.lucy.screens.projects

import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.classes.item.Item

sealed interface ProjectsChannel {
    data class ConfirmDelete(val item: Item): ProjectsChannel
    data class Edit(val item: Item): ProjectsChannel
    data class Navigate(val key: NavKey): ProjectsChannel
    data object ShowAddItemDialog: ProjectsChannel
    data class ShowMessage(val message: String) : ProjectsChannel
}