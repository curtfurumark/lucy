package se.curtrune.lucy.screens.daycalendar

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.calender.Week
import java.time.LocalDate

data class DayCalendarState(
    var currentItem: Item? = null,
    var editItem: Boolean = false,
    var items: List<Item> = mutableListOf(),
    val date: LocalDate = LocalDate.now(),
    var errorMessage: String = "",
    var selectedTabIndex: Int = 1,
    var showPostponeDialog: Boolean = false,
    var showTabs: Boolean = false,
    var showStats: Boolean = false,
    var currentParent: Item? = null,
    var tabs: List<Item> = mutableListOf(),
    //var tabStack: T,
    var currentWeek: Week = Week(),
    val numberWeeks: Int = 10,
    val currentWeekPage: Int = 5
)