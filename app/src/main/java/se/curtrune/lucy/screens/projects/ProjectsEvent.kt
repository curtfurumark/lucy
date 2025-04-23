package se.curtrune.lucy.screens.projects

import se.curtrune.lucy.classes.item.Item

sealed interface ProjectsEvent{
    data class Delete(val item: Item): ProjectsEvent
    data class OnItemClick(val item: Item) : ProjectsEvent
    data class UpdateItem(val item: Item) : ProjectsEvent
    data class OnLongItemClick(val item: Item) : ProjectsEvent
    data class OnTabClick(val tab: ProjectTab) : ProjectsEvent
    data class InsertItem(val item: Item) : ProjectsEvent
    data object ShowAddItemDialog: ProjectsEvent
}