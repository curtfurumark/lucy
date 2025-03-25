package se.curtrune.lucy.screens.dev

sealed interface DevChannel {
    data class ShowNavigationDrawer(val show: Boolean): DevChannel
    data object NavigateToDayCalendar: DevChannel
}