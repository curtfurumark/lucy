package se.curtrune.lucy.screens.first

sealed interface LogInEvent {
    data class LogIn(val pwd: String): LogInEvent
}