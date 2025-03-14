package se.curtrune.lucy.screens.message_board

sealed interface MessageBoardEvent {
    data class NewMessage(val message: Message): MessageBoardEvent
    data object OnAddMessageClick: MessageBoardEvent
}