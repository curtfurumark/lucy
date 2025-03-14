package se.curtrune.lucy.screens.todo

import se.curtrune.lucy.classes.item.Item

data class TodoState(
    val items : List<Item> = emptyList()
)