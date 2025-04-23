package se.curtrune.lucy.screens.projects

import se.curtrune.lucy.activities.kotlin.composables.DialogSettings
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.add_item.DefaultItemSettings

data class ProjectsState(
    val items: List<Item> = emptyList(),
    val tabs: List<ProjectTab> = emptyList(),
    val currentParent: Item? = null,
    val defaultItemSettings: DefaultItemSettings = DefaultItemSettings(),
)
