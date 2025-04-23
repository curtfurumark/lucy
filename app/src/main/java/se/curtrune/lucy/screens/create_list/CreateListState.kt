package se.curtrune.lucy.screens.create_list

import se.curtrune.lucy.classes.item.Item

data class CreateListState(
    var parent: Item? = null,
    var items: List<String> = emptyList(),
    val focusIndex: Int = 0
)