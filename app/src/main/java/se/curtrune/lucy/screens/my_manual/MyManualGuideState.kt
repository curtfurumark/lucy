package se.curtrune.lucy.screens.my_manual

import se.curtrune.lucy.classes.item.Item

data class MyManualGuideState(
    var currentPage: Int = 0,
    var medicines: List<Item> = emptyList(),
    var triggers: List<Item> = emptyList(),
    var sensories: List<Item> = emptyList(),
    var name: String = "",
    var height: Float = 0.0f
)