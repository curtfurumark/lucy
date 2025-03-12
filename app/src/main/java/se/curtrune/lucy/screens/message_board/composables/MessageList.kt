package se.curtrune.lucy.screens.message_board.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.message_board.MessageBoardEvent


@Composable
fun MessageList(state: MessageBoardState, onEvent: (MessageBoardEvent)->Unit){
    LazyColumn (modifier = Modifier.fillMaxWidth()){
        items(state.messages){ message->
            MessageView(message = message)
        }
    }
}