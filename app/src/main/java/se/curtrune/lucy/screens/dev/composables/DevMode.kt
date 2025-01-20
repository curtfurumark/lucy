package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DevMode(){
    Card(modifier = Modifier.fillMaxWidth()){
        Row(){
            Text(text = "dev mode")
            //Switch()
        }
    }
}