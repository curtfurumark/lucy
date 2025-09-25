package se.curtrune.lucy.activities.share.composables

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImageViewer(modifier: Modifier = Modifier,uri: Uri, contentDescription: String ){
    Image(
        painter = rememberAsyncImagePainter(model = uri),
        contentDescription = contentDescription,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Fit
    )
}