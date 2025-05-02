package se.curtrune.lucy.screens.log_in

sealed interface LogInEvent {
    data class LogIn(val pwd: String): LogInEvent
}