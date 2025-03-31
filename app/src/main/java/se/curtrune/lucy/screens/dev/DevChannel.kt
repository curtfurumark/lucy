package se.curtrune.lucy.screens.dev

import se.curtrune.lucy.app.FirstPage

sealed interface DevChannel {
    data class ShowNavigationDrawer(val show: Boolean): DevChannel
    data object NavigateToDayCalendar: DevChannel
    data object NavigateToWeekCalendar : DevChannel
    data class Navigate(val fragment: FirstPage): DevChannel
}