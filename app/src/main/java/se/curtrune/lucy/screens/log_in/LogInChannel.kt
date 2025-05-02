package se.curtrune.lucy.screens.log_in

import se.curtrune.lucy.app.InitialScreen

sealed interface LogInChannel {
    data class  navigate(val initialScreen: InitialScreen): LogInChannel
    data class showMessage(val message: String): LogInChannel
}