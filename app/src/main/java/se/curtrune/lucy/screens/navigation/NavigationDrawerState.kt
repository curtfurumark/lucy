package se.curtrune.lucy.screens.navigation

data class NavigationDrawerState(
    var showMedicineLink: Boolean = false,
    var showProjectsLink: Boolean = true,
    var showDurationLink: Boolean = true,
    var showAppointmentsLink: Boolean =false,
    var showHealthLink: Boolean = false
)