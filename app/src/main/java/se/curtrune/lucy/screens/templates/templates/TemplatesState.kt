package se.curtrune.lucy.screens.templates.templates

import se.curtrune.lucy.classes.item.Item

data class TemplatesState(
    var selectedTemplate: Item? = null,
    val templates: List<Item> = emptyList(),
)