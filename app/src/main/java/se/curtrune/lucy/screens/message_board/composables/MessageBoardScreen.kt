package se.curtrune.lucy.screens.message_board.composables

import androidx.compose.runtime.Composable
import se.curtrune.lucy.screens.message_board.MessageBoardEvent

@Composable
fun MessageBoardScreen(state: MessageBoardState, onEvent: (MessageBoardEvent)->Unit){
    MessageList(state = state, onEvent = onEvent)

}