package se.curtrune.lucy.screens.log_in

import se.curtrune.lucy.app.InitialScreen

data class LogInState(
    val usesPassWord: Boolean = false,
    val initialScreen: InitialScreen = InitialScreen.CALENDER_DATE
)
