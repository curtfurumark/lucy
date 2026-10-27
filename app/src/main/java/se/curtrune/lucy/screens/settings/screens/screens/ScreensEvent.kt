package se.curtrune.lucy.screens.settings.screens.screens

sealed interface ScreensEvent {
    data class ShowAppointments(val show: Boolean): ScreensEvent

    data class ShowDevScreen(val visible : Boolean) : ScreensEvent
    data class ShowMedicine(val visible : Boolean) : ScreensEvent
    data class ShowMentalStats(val visible: Boolean): ScreensEvent
    data class ShowProjects(val visible : Boolean) : ScreensEvent
    data class ShowDuration(val visible : Boolean) : ScreensEvent
    data class ShowTimeLine(val visible : Boolean) : ScreensEvent
    data class  ShowToDo(val visible : Boolean) : ScreensEvent
}