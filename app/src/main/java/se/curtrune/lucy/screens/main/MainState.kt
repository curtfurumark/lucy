package se.curtrune.lucy.screens.main

import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.web.VersionInfo

data class MainState(
    val mental: Mental = Mental(),
    val versionInfo: VersionInfo? = null
)
