package se.curtrune.lucy.screens.templates.create

import se.curtrune.lucy.classes.item.Item

data class CreateTemplateState(
    val template: Item = Item(),
    val items: List<Item> = emptyList(),
)