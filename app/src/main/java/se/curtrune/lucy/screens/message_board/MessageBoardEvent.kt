package se.curtrune.lucy.screens.message_board

sealed interface MessageBoardEvent {
    data class NewMessage(val message: Message): MessageBoardEvent
    data class SelectedCategory(val category: String): MessageBoardEvent
    data class OnMessageClick(val message: Message): MessageBoardEvent
    data class UpdateMessage(val message: Message) : MessageBoardEvent
    data class Search(val filter: String, val everywhere: Boolean) : MessageBoardEvent
    data object OnAddMessageClick: MessageBoardEvent
    data object OnAddMessageDismiss : MessageBoardEvent
}