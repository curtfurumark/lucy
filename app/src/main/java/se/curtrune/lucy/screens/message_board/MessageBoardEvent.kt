package se.curtrune.lucy.screens.message_board

import se.curtrune.lucy.classes.Message

sealed interface MessageBoardEvent {
    data class NewMessage(val message: Message): MessageBoardEvent
}