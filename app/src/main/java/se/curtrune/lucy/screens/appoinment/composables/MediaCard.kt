package se.curtrune.lucy.screens.appoinment.composables

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.item.Item
import androidx.core.net.toUri
import se.curtrune.lucy.screens.appoinment.AppointmentEvent


@Composable
fun MediaListCard(items: List<Item>, onItemClicked: (Item)->Unit, onEvent: (AppointmentEvent)->Unit) {
    Card(modifier = Modifier.fillMaxWidth()){
        LazyColumn() {
            item{Text(text = "media ")}
            item{Spacer(modifier = Modifier.height(4.dp))}
            items(items.size){
                MediaItem(
                    item = items[it],
                    onItemClicked = onItemClicked,
                    onEvent = onEvent)
            }
        }
    }
}
@Composable
fun MediaItem(item: Item, onItemClicked: (Item)->Unit, onEvent: (AppointmentEvent)->Unit){
    val context = LocalContext.current
    println("MediaItem(item: ${item.heading}, desc: ${item.comment})")
    val uri =item.comment.toUri()
    println("uri: $uri")
    val mimeType =context.contentResolver.getType(item.comment.toUri())
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween) {
        Text(
            text = "media: ${item.heading} $mimeType",
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable(onClick = {
                    onItemClicked(item)
                }
           )
        )
        Icon(
            modifier = Modifier.padding(end = 8.dp)
                .clickable(onClick = {onEvent(AppointmentEvent.Delete(item))}),
            imageVector = Icons.Filled.Delete,
            contentDescription = "delete")
    }
}

