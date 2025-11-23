package se.curtrune.lucy.screens.bullet_list

import se.curtrune.lucy.classes.item.Item

data class BulletListState(
    val focusIndex: Int = 0,
    val heading: String = "",
    val items: List<Item> = emptyList()
)
