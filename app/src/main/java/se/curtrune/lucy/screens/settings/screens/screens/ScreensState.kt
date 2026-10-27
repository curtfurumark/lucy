package se.curtrune.lucy.screens.settings.screens.screens

data class ScreensState(
    var showAppointments: Boolean = true,
    var showDevScreen: Boolean = false,
    var showDuration: Boolean = false,
    var showProjects: Boolean = false,
    var showMedicine: Boolean = false,
    var showMyManual: Boolean= false,
    var showTimeLine: Boolean = true,
    var showToDo: Boolean = false,
    var showMentalStats: Boolean = false
)