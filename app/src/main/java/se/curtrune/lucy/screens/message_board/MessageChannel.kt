package se.curtrune.lucy.screens.message_board

sealed interface MessageChannel {
    data object ShowAddMessageBottomSheet: MessageChannel
    data class ShowSnackBar(val message: String): MessageChannel
}