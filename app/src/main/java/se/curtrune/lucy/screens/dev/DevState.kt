package se.curtrune.lucy.screens.dev

import se.curtrune.lucy.classes.Mental

data class DevState(
    var systemInfoList: List<SystemInfo> = mutableListOf(),
    var mental: Mental? = null
)
