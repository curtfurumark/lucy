package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import se.curtrune.lucy.classes.item.Item

@Composable
fun TagsCard(item: Item, onTagsChanged: (String)->Unit){
    var tags by remember {
        mutableStateOf(item.tags)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "tags")},
            singleLine = true,
            value = tags, onValueChange = {
            tags = it
            onTagsChanged(it)
        })

    }
}