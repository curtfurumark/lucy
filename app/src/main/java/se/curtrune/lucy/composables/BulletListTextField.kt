package se.curtrune.lucy.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun BulletListTextField(){
    var lines = listOf<String>()
    var currentLine by remember {
        mutableStateOf("* ")
    }
    Card(
        modifier = Modifier.fillMaxWidth()){
        TextField( value = currentLine, onValueChange = {
            currentLine = it
        })

    }
}