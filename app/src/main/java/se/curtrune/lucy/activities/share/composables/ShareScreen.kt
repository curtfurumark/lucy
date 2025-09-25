package se.curtrune.lucy.activities.share.composables

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.share.ShareViewModel
import se.curtrune.lucy.classes.item.Item
/**
 * assumption:
 * sharedText is a url with a heading for example:
 * "search the web for: https://www.google.com"
 *
 */
fun parseText(text: String): Item{
    val url = text.substring(text.indexOf("https://"))
    println(url)
    val heading = text.substring(0, text.indexOf("https://"))
    val item = Item().also {
        it.heading = heading
        it.comment = url
    }
    return item
}

@Composable
fun ShareImageScreen( modifier: Modifier = Modifier, uri: Uri){

}

@Composable
fun ShareScreen(modifier: Modifier = Modifier, sharedText: String?){
    val viewModel: ShareViewModel = viewModel()
    if( sharedText == null){
        Text(text = "no shared text")
        return
    }
    val item = parseText(sharedText)
    //viewModel.onEvent(ShareEvent.AddItem(item))
    Column(
        modifier = modifier.fillMaxSize()
            .padding(16.dp)) {
        Text(text = "save to notes", style = MaterialTheme.typography.headlineLarge)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = item.heading,
            onValueChange = {item.heading = it}
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = item.comment,
            onValueChange = {item.comment = it})
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Button(onClick = {}){
                Text(text = "dismiss")
            }
            Button(
                onClick = {
                    viewModel.onEvent(ShareEvent.Save(item))
                    println("save url")}){
                Text(text = "save")
            }
        }
    }

}

@Preview
@Composable
fun ShareScreenPreview(){
    ShareScreen(sharedText = "test")
}