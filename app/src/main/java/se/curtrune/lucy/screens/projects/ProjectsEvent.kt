package se.curtrune.lucy.screens.projects

import se.curtrune.lucy.classes.item.Item

sealed interface ProjectsEvent{
    data class Delete(val item: Item): ProjectsEvent
    data class OnItemClick(val item: Item) : ProjectsEvent
    data class UpdateItem(val item: Item) : ProjectsEvent
    data class OnLongItemClick(val item: Item) : ProjectsEvent
    data class OnTabClick(val tab: String) : ProjectsEvent
    data object Descend: ProjectsEvent
    data object Ascend: ProjectsEvent
}