package se.curtrune.lucy.screens.file_attach_screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.classes.item.Item

@Composable
fun FileAttachScreen(
    modifier: Modifier = Modifier,
    item: Item,
    navigate: (NavKey)->Unit,
    onBack: ()->Unit
){
    val viewModel = viewModel<AttachFileViewModel>(){
        AttachFileViewModel(item)
    }
    val state by viewModel.state.collectAsState()
    var showFilePicker by remember {
        mutableStateOf(false)
    }
    var fileUri by remember {
        mutableStateOf<String>("")
    }
    var path by remember {
        mutableStateOf<String?>(null)
    }
    val context = LocalContext.current
    var mimeType by remember {
        mutableStateOf<String?>(null)
    }
    var heading by remember {
        mutableStateOf<String>(state.heading)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        showFilePicker = false
        uri?.let {
            println("uri: $it")
            path = it.path
            fileUri = it.toString()
            viewModel.onFileSelected(it)
            mimeType = context.contentResolver.getType(uri)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        OutlinedTextField(
            value = heading,
            onValueChange = {
                heading = it
                viewModel.onEvent(AttachFileEvent.OnHeadingChanged(it))
            },
            label = {
                Text(text = "heading")
            },
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "item: ${state.item.heading}")
        mimeType?.let { Text(text= it) }
        Text(text = "file uri: $fileUri")
        Text(text = "path: ${path ?: "null"}")
        Button(onClick = { showFilePicker = true }){
            Text(text = "attach file")
        }
        Button(onClick = { onBack() }) {
            Text(text = "back")
        }
    }
    if (showFilePicker) {
        SideEffect {
            launcher.launch(arrayOf("*/*"))
        }
    }
}


@Composable
@Preview
fun PreviewAttachFileScreen(){
    FileAttachScreen(item = Item("hello"), navigate = {}, onBack = {})

}