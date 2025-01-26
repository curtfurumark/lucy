package se.curtrune.lucy.screens.daycalendar

import se.curtrune.lucy.classes.Item
import java.time.LocalDate

data class DayCalendarState(
    var currentItem: Item? = null,
    var editItem: Boolean = false,
    var items: List<Item> = mutableListOf(),
    val date: LocalDate = LocalDate.now(),
    var errorMessage: String = "",
    var showPostponeDialog: Boolean = false,
    var showTabs: Boolean = false,
    var showStats: Boolean = false,
    var currentParent: Item? = null,
    var tabItems: List<Item>? = null,
    var tabStack: TabStack? = null
)