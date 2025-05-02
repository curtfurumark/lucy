package se.curtrune.lucy.screens.main

import se.curtrune.lucy.classes.Mental

data class TopAppBarState(
    var title: String = "lucinda",
    val filter: String = "",
    val searchEverywhere: Boolean = false,
    val mental: Mental = Mental(),
    val showMental: Boolean = true
)
