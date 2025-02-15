package se.curtrune.lucy.composables.top_app_bar

sealed interface TopAppBarEvent {
    data object DayCalendar: TopAppBarEvent
    data object Menu: TopAppBarEvent
    data class OnSearch(val filter: String, val everywhere: Boolean): TopAppBarEvent
    data object OnPanic: TopAppBarEvent
    data object OnBoost: TopAppBarEvent
}