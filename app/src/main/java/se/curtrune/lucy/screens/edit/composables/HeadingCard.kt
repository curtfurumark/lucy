package se.curtrune.lucy.screens.edit.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun HeadingCard(heading: String, onHeadingChanged: (String) -> Unit){
    var title by remember {
        mutableStateOf(heading)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                onHeadingChanged(it)
            }
        )
    }
}