package se.curtrune.lucy.activities.kotlin.composables

import se.curtrune.lucy.classes.item.Item
import java.time.LocalDate
import java.time.LocalTime

data class DialogSettings(var isCalendarItem: Boolean = false,
                          var targetTime: LocalTime = LocalTime.ofSecondOfDay(0),
                          var targetDate: LocalDate = LocalDate.ofEpochDay(0),
                          var parent: Item? = null,
                          var isAppointment: Boolean = false
)

