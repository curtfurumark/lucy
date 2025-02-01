package se.curtrune.lucy.screens.todo

import se.curtrune.lucy.classes.Item

data class TodoState(
    val items : List<Item> = emptyList()
)