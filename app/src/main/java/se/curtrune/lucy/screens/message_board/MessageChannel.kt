package se.curtrune.lucy.screens.message_board

sealed interface MessageChannel {
    data object ShowAddMessageBottomSheet: MessageChannel
    data class  ShowProgressBar(val show: Boolean): MessageChannel
    data class ShowSnackBar(val message: String): MessageChannel
}