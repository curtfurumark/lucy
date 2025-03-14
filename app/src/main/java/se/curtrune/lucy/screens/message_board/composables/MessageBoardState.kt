package se.curtrune.lucy.screens.message_board.composables

import se.curtrune.lucy.screens.message_board.Message

data class MessageBoardState(
    val messages: List<Message> = emptyList()
)
