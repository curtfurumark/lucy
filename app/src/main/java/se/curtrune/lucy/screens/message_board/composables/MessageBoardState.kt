package se.curtrune.lucy.screens.message_board.composables

import se.curtrune.lucy.classes.Message

data class MessageBoardState(
    val messages: List<Message> = emptyList()
)
