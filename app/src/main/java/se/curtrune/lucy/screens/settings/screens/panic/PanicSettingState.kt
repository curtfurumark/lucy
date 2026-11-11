package se.curtrune.lucy.screens.settings.screens.panic

import se.curtrune.lucy.classes.PanicOption

data class PanicSettingState(
    val phoneNumber: Int = -1,
    val panicOption: PanicOption = PanicOption.PENDING
)