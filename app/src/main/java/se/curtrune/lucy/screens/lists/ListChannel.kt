package se.curtrune.lucy.screens.lists

sealed interface ListChannel {
    data class ShowMessage(val message: String): ListChannel
}