package se.curtrune.lucy.screens.main

data class TopAppBarState(
    var title: String = "lucinda",
    val filter: String = "",
    val everywhere: Boolean = false
)
