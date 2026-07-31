package se.curtrune.lucy.screens.attach_image

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.classes.item.Item
import java.io.File
import java.util.Objects


@Composable
fun AttachImageScreen(modifier: Modifier = Modifier, item: Item, navigate: (NavKey)->Unit   ){
    val viewModel = viewModel<AttachImageViewModel>(){
        AttachImageViewModel(item)
    }
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val tempUri = createTempPictureUri(context)

    val uri = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Handle the successful capture, e.g., notify the viewModel
            // viewModel.onImageCaptured(tempUri)
            viewModel.onEvent(AttachImageEvent.OnImageSuccess(tempUri.toString()))
        }else{
            println("error capturing image")
            viewModel.onEvent(AttachImageEvent.OnImageFailure)
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "item: ${state.item.heading}")
        Text(text = "tempUri: ${tempUri}")
        Button(onClick = {
            // Use the launcher to take a photo and save it to the tempUri
            uri.launch(tempUri)
        }){
            Text(text = "attach image")
        }

    }


}

private fun createTempPictureUri(context: Context): Uri {
    val tempFile = File.createTempFile("picture_${System.currentTimeMillis()}", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(Objects.requireNonNull(context),
        "se.curtrune.lucy.fileprovider", tempFile)
}