package se.curtrune.lucy.screens.message_board.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.message_board.MessageBoardEvent
import se.curtrune.lucy.screens.message_board.MessageBoardState

@Composable
fun MessageBoardScreen(
    modifier: Modifier = Modifier,
    state: MessageBoardState,
    onEvent: (MessageBoardEvent)->Unit){
    Column(modifier = Modifier.fillMaxWidth()) {
        MessageBoardButton(state = state, onEvent = onEvent)
        MessageList(state = state, onEvent = onEvent)
    }

}