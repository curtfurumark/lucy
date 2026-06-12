package se.curtrune.lucy.screens.appoinment.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.classes.item.Item

@Composable
fun ContactCard(contact: Item){
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(text = "add contact")
    }
}