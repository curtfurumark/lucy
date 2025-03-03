package se.curtrune.lucy.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NavigationDrawer(){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "hello drawer")
    }
}