package se.curtrune.lucy.screens.top_appbar

sealed interface TopAppBarEvent {
    data object DayCalendar: TopAppBarEvent
    data object DrawerMenu: TopAppBarEvent
    data object ActionMenu: TopAppBarEvent
    data class OnSearch(val filter: String, val everywhere: Boolean): TopAppBarEvent
    data object OnPanic: TopAppBarEvent
    data object OnBoost: TopAppBarEvent
    data object DayClicked : TopAppBarEvent
    data object WeekClicked : TopAppBarEvent
    data object MonthClicked : TopAppBarEvent
    data object MedicinesClicked : TopAppBarEvent
    data object SettingsClicked : TopAppBarEvent
    data object DevActivity : TopAppBarEvent
    data object CheckForUpdate : TopAppBarEvent
}