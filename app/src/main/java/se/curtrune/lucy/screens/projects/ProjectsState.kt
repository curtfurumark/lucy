package se.curtrune.lucy.screens.projects

import se.curtrune.lucy.classes.item.Item

data class ProjectsState(
    val items: List<Item> = emptyList(),
    val tabs: List<ProjectTab> = emptyList(),
    val currentParent: Item? = null,
)
