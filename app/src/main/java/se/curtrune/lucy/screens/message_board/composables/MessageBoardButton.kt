package se.curtrune.lucy.screens.message_board.composables

import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.message_board.MessageBoardEvent
import se.curtrune.lucy.screens.message_board.MessageBoardState

@Composable
fun MessageBoardButton(
        modifier: Modifier = Modifier,
        state: MessageBoardState,
        onEvent: (MessageBoardEvent)->Unit){
            val options = listOf("message", "todo")
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed{ index, label->
            SegmentedButton(onClick = {
                selectedIndex = index
                onEvent(MessageBoardEvent.SelectedCategory(options[selectedIndex]))
            },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                selected = index == selectedIndex
            ) {
                Text(label)
            }
        }
    }
}

@Composable
@Preview
fun PreviewMessageButton(){
    LucyTheme {
        MessageBoardButton(state = MessageBoardState(), onEvent = {})
    }
}