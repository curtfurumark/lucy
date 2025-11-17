package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.item.Item

@Composable
fun ParentCard(modifier: Modifier = Modifier, item: Item){
    Card(modifier = modifier.fillMaxWidth()){
        Text(
            modifier = Modifier.padding(8.dp),
            text = "parent id: ${item.parentId}")
    }
}