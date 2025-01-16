package se.curtrune.lucy.activities.kotlin.composables

import java.time.LocalDate
import java.time.LocalTime

data class ItemSettings(var isCalendarItem: Boolean = false,
                        var targetTime: LocalTime = LocalTime.ofSecondOfDay(0),
                        var targetDate: LocalDate = LocalDate.ofEpochDay(0)
)

