package se.curtrune.lucy.screens.message_board

data class MessageBoardState(
    val messages: List<Message> = emptyList(),
    val category: String = "messages",
    var currentMessage: Message? = null
)
