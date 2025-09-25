package se.curtrune.lucy.screens.settings

enum class PanicOption {
    URL, ICE, GAME, PENDING
}

fun test(){
    val panicOptions = PanicOption.entries.toTypedArray()
}