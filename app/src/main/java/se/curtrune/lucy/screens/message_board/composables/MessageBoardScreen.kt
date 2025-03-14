package se.curtrune.lucy.screens.message_board.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.message_board.MessageBoardEvent

@Composable
fun MessageBoardScreen(
    modifier: Modifier = Modifier,
    state: MessageBoardState,
    onEvent: (MessageBoardEvent)->Unit){
    MessageList(state = state, onEvent = onEvent)

}