package se.curtrune.lucy.screens.templates.edit

import se.curtrune.lucy.classes.item.Item

data class EditTemplateState(
    val template: Item = Item(),
    val children: List<Item> = emptyList(),
    val xxx: MutableList<String> = mutableListOf()
)
