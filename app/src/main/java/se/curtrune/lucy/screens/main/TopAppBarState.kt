package se.curtrune.lucy.screens.main

import se.curtrune.lucy.classes.Mental

data class TopAppBarState(
    var title: String = "lucinda",
    val filter: String = "",
    val everywhere: Boolean = false,
    val mental: Mental = Mental()
)
