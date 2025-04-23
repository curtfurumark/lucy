package se.curtrune.lucy.screens.message_board

import se.curtrune.lucy.screens.message_board.composables.DefaultMessage

data class MessageBoardState(
    val messages: List<Message> = emptyList(),
    val category: String = "messages",
    val defaultMessage: DefaultMessage = DefaultMessage()
)
