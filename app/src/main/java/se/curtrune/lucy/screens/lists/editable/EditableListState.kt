package se.curtrune.lucy.screens.lists.editable

import se.curtrune.lucy.classes.item.Item

data class EditableListState(
    val listItems: List<Item> = emptyList(),
    val item: Item = Item(),
    val focusIndex: Int = 0
    )