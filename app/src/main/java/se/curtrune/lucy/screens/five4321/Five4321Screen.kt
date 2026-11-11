package se.curtrune.lucy.screens.five4321

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.navigation.Route

@Composable
fun Five4321Screen(modifier: Modifier = Modifier){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(text = "54321")
    }
}