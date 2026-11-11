package se.curtrune.lucy.screens.settings.screens.panic

sealed interface PanicSettingChannel {
    data class Message(val message: String): PanicSettingChannel
}