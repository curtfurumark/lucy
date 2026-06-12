package se.curtrune.lucy.screens.appoinment.composables

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
fun HeadingCard(item: Item, onHeadingChange: (String)->Unit){
    var heading by remember {
        mutableStateOf(item.heading)
    }
    //Card(modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = heading,
            onValueChange = {
                heading = it
                onHeadingChange(it)
            },
            label = { Text(text = "heading") }
        )
    //}
}