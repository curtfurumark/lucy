package se.curtrune.lucy.screens.settings.screens.panic

import se.curtrune.lucy.classes.PanicOption

sealed interface PanicSettingEvent {
    data class SetICE(val number: Int): PanicSettingEvent
    data class SetBreathe(val breath: Boolean): PanicSettingEvent
    data class SetUrl(val url: String): PanicSettingEvent
    data class SetUrlOption(val checked: Boolean): PanicSettingEvent
    data class SetPanicOption(val option: PanicOption): PanicSettingEvent
}