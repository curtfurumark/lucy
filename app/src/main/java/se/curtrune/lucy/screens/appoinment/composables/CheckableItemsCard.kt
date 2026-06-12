package se.curtrune.lucy.screens.appoinment.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.classes.item.Item

@Composable
fun CheckableItemsCard(items: List<Item> = listOf(), onCheckChange: (Item)->Unit) {
    Card(modifier = Modifier.fillMaxWidth()){
        Text(text = "checklist")
        items.forEach {
            CheckableItem(item = it, onCheckChange = {
                onCheckChange(it)
            })
        }
    }
}
