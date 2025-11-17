package se.curtrune.lucy.screens.navigation

data class NavigationDrawerState(
    var showMedicineLink: Boolean = false,
    var showProjectsLink: Boolean = true,
    var showDevScreenLink: Boolean = false,
    var showDurationLink: Boolean = false,
    var showAppointmentsLink: Boolean =false,
    var showHealthLink: Boolean = false,
    var showTodoScreen: Boolean = false,
    var showMentalStats: Boolean = false

)