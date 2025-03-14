package se.curtrune.lucy.screens.dev

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.screens.dev.composables.MyTab

data class DevState(
    var systemInfoList: List<SystemInfo> = mutableListOf(),
    var tabs: List<MyTab> = mutableListOf(),
    var mental: Mental? = null,
    var items: List<Item> = emptyList(),
    var currentItem: Item? = null
)
