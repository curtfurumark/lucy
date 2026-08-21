package se.curtrune.lucy.screens.navigation

data class NavigationDrawerState(
    var showAppointmentsLink: Boolean =false,
    var showDevScreenLink: Boolean = false,
    var showDurationLink: Boolean = false,
    var showHealthLink: Boolean = false,
    var showListLink: Boolean =false,
    var showMedicineLink: Boolean = false,
    var showMentalStats: Boolean = false,
    var showMyManual: Boolean = false,
    var showProjectsLink: Boolean = false,
    var showTimeLine: Boolean = false,
    var showTodoScreen: Boolean = false,
)