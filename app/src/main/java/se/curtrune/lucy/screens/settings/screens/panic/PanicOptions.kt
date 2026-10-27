package se.curtrune.lucy.screens.settings.screens.panic

enum class PanicOption {
    URL, ICE, GAME, MEDITATION, POSTPONE, PENDING
}

fun test(){
    val panicOptions = PanicOption.entries.toTypedArray()
}