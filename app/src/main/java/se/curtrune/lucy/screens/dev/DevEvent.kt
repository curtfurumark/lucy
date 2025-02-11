package se.curtrune.lucy.screens.dev

import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.screens.daycalendar.DateEvent
import java.util.Date

sealed interface DevEvent{
    data object CreateItemTree: DevEvent
    data object ResetApp: DevEvent
    data class Search(val query: String, val everywhere: Boolean): DevEvent
}