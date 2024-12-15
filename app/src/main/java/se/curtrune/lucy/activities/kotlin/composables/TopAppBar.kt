package se.curtrune.lucy.activities.kotlin.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LucindaTopAppBar(){
    TopAppBar(title = {Text(text = "top app bar", color = Color.White)})
}