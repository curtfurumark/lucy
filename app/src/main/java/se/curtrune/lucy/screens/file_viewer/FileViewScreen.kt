package se.curtrune.lucy.screens.file_viewer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import se.curtrune.lucy.classes.item.Item

@Composable
fun FileViewerScreen(item: Item, onBack: ()->Unit) {
    val context = LocalContext.current
    val mimeType by remember {
        mutableStateOf(context.contentResolver.getType(item.comment.toUri()))
    }

    val pdfBitmaps by produceState<List<Bitmap>>(initialValue = emptyList(), item.comment) {
        if (mimeType == "application/pdf") {
            val bitmaps = mutableListOf<Bitmap>()
            try {
                context.contentResolver.openFileDescriptor(item.comment.toUri(), "r")?.use { pfd ->
                    val renderer = PdfRenderer(pfd)
                    for (i in 0 until renderer.pageCount) {
                        renderer.openPage(i).use { page ->
                            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                            bitmaps.add(bitmap)
                        }
                    }
                    renderer.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            value = bitmaps
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        Text(text = "file viewer")
        Text(text = "id ${item.id}")
        Text(text = "type ${item.type}")
        Text(text = "mime type $mimeType")
        Text(text = "heading ${item.heading}")
        Text(text = "description ${item.description}")
        Text(text = "COM: ${item.comment}")
        Text(text = "URI: ${item.comment.toUri()}")
        if (mimeType == "image/jpeg")  {
            AsyncImage(
                model = item.comment,
                contentDescription = "image content",
                modifier = Modifier.size(400.dp),
                contentScale = ContentScale.Fit)
        }else if(mimeType == "application/pdf"){
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(pdfBitmaps.size) { index ->
                    Image(
                        bitmap = pdfBitmaps[index].asImageBitmap(),
                        contentDescription = "Pdf page ${index + 1}",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } else {
            Text(text = "not an image, not a pdf")
        }
    }
}