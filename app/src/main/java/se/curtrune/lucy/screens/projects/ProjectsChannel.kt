package se.curtrune.lucy.screens.projects

import se.curtrune.lucy.classes.item.Item

sealed interface ProjectsChannel {
    data class Edit(val item: Item): ProjectsChannel
}