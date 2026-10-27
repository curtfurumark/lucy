package se.curtrune.lucy.screens.projects

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.projects.composables.DropDownItems

sealed interface ProjectsEvent{
    data class Delete(val item: Item): ProjectsEvent
    data class OnDropDownItemClicked(val item: Item, val action: DropDownItems): ProjectsEvent
    data class OnItemClick(val item: Item) : ProjectsEvent
    data class UpdateItem(val item: Item) : ProjectsEvent
    data class OnLongItemClick(val item: Item) : ProjectsEvent
    data class OnTabClick(val tab: ProjectTab) : ProjectsEvent
    data class InsertItem(val item: Item) : ProjectsEvent
    data class RequestDelete(val item: Item): ProjectsEvent
    data object ShowAddItemDialog: ProjectsEvent
}