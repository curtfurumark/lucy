package se.curtrune.lucy.screens.main

import androidx.fragment.app.Fragment
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.screens.daycalendar.CalendarDayFragment
import se.curtrune.lucy.web.VersionInfo

data class MainState(
    val mental: Mental = Mental(),
    val versionInfo: VersionInfo? = null,
    val currentFragment: LucindaFragment = LucindaFragment.DayCalendar
)
