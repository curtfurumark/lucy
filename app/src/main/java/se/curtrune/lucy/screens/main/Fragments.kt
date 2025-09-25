package se.curtrune.lucy.screens.main

import android.os.Bundle
import se.curtrune.lucy.app.Lucinda
import se.curtrune.lucy.classes.item.Item

sealed interface LucindaFragment {
    data object DayCalendar: LucindaFragment
    data object WeekCalendar: LucindaFragment
    data object MonthCalendar : LucindaFragment
    data object Settings: LucindaFragment
    data class ItemEditor(val bundle: Bundle) : LucindaFragment
}