package se.curtrune.lucy.screens.my_manual

data class MyManualData(
    private val name: String = "",
    private val height: String = "",
    private val triggers: List<String> = emptyList(),
    private val medicines: List<String> = emptyList(),
)