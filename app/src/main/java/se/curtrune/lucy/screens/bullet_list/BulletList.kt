package se.curtrune.lucy.screens.bullet_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.item.Item

@Composable
fun BulletList(state: BulletListState, event: (BulletListEvent) -> Unit){
    var heading by remember { mutableStateOf("heading") }
    var currentFocus by remember {
        mutableIntStateOf(0) }
    val focusRequester = remember { FocusRequester() }
    Column(modifier = Modifier
        .fillMaxSize()
        .imePadding()) {
        Text(text = "bullet list with ${state.items.size} items")
        Text(text = "current focus $currentFocus")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = heading,
            onValueChange = { heading = it }
        )
    }
}
@Composable
fun BulletItem2(item: Item,hasFocus: Boolean, onEvent: (BulletListEvent) -> Unit){
    var heading by remember { mutableStateOf("heading") }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(hasFocus){
        if(hasFocus){
            focusRequester.requestFocus()
        }
    }
    OutlinedTextField(
        modifier = Modifier.focusRequester(focusRequester),
        value = heading,
        onValueChange = {heading = it})

}
