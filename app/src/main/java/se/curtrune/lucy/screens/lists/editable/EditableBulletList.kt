package se.curtrune.lucy.screens.lists.editable

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item

@Composable
fun EditableBulletList() {
    val viewModel: EditableListViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    //var items by remember { mutableStateOf(listOf("")) }
    var heading by remember { mutableStateOf(state.item.heading) }
    var listRoot by remember { mutableStateOf(state.item) }
    LazyColumn {
        item {
            Text(text = "${state.focusIndex}")
        }
        item{
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    viewModel.onEvent(EditableListEvent.SaveList)
                }) {
                    Text(text = "save list")
                }
                Button(onClick = {
                    viewModel.onEvent(EditableListEvent.Dismiss)
                }){
                    Text(text = "cancel")
                }

            }
        }
        item{
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = heading,
                onValueChange = {
                    //viewModel.onEvent(EditableListEvent.ChangeHeading(it))
                    heading = it
                    listRoot.heading = it
                    viewModel.onEvent(EditableListEvent.UpdateListRoot(listRoot))

                                }
            , label = { Text("name") })
        }
        itemsIndexed(state.listItems, key = { _, item -> item.id }) { index, item ->
            println("itemsIndexed $index, item ${item.heading}")
            BulletTextField(
                item = item,
                index = index,
                hasFocus = index == state.focusIndex,
                onEvent = viewModel::onEvent,
            )
        }
    }
    LaunchedEffect(Unit) {
        viewModel.channel.collect {
            when(it) {
                is EditableListChannel.Message -> {
                    println("...message ${it.message}")
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                is EditableListChannel.Navigate -> {
                    println("...navigate ${it.navKey}")
                }
            }
        }
    }
}

@Composable
fun BulletBasicTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp, horizontal = 8.dp)) {

        Text("• ", modifier = Modifier.padding(end = 4.dp), color = Color.Red)

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = LocalTextStyle.current.copy(
                color = Color.White,
                fontSize = 24.sp),
            modifier = Modifier
                .fillMaxWidth(),
            //textColor = Color.Blue // Example: Change TextField text color
        )
    }
}
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
        .padding(vertical = 4.dp, horizontal = 8.dp)) {

        TextField(
            leadingIcon = {Icon(imageVector = Icons.Default.Circle, contentDescription = "list item")},
            value = heading,
            onValueChange = {
                if( it.endsWith("\n")){
                    //onEnter(index)
                    onEvent(EditableListEvent.AddItem(index))
                }else{
                    heading = it
                    item.heading = it
                    onEvent(EditableListEvent.Update(item))
                } },
            textStyle = LocalTextStyle.current.copy(
                color = Color.White,
                fontSize = 24.sp),
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

@Composable
@PreviewLightDark
fun PreviewEditableBulletList(){
    LucyTheme {
        EditableBulletList()

    }
}