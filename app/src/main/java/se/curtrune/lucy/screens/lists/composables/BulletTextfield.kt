package se.curtrune.lucy.screens.lists.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.lists.editable.EditableListEvent


@Composable
fun BulletTextField(
    item: Item,
    index: Int,
    hasFocus: Boolean = false,
    onEvent: (EditableListEvent) -> Unit,
) {
    val item by remember {
        mutableStateOf(item)
    }
    var focusRequester by remember {
        mutableStateOf(FocusRequester())
    }
    var heading by remember {
        mutableStateOf(item.heading)
    }
    LaunchedEffect(hasFocus) {
        if (hasFocus) {
            focusRequester.requestFocus()
        }
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding( horizontal = 8.dp)) {

        TextField(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Circle,
                    contentDescription = "list item",
                    modifier = Modifier.height(8.dp)
                )
            },
            value = heading,
            onValueChange = {
                if( it.endsWith("\n")){
                    //onEnter(index)
                    onEvent(EditableListEvent.AddItem(index))
                }else if(it.isEmpty()) {
                    onEvent(EditableListEvent.RemoveItem(index))
                }else{
                    heading = it
                    item.heading = it
                    onEvent(EditableListEvent.Update(item))
                } },
            label = {Text(text = "$index")},
            textStyle = LocalTextStyle.current.copy(
                color = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
            ,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
            )
        )
    }
}