package se.curtrune.lucy.screens.lists

data class ListState(
    val lists: List<String> = emptyList(),
    val optionsVisible: Boolean = true,
    val noteVisible: Boolean = false,
    val listVisible: Boolean = false
)
