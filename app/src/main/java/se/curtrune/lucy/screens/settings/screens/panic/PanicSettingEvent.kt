package se.curtrune.lucy.screens.settings.screens.panic

sealed interface PanicSettingEvent {
    data class SetICE(val number: Int): PanicSettingEvent
    data class SetBreathe(val breath: Boolean): PanicSettingEvent
}