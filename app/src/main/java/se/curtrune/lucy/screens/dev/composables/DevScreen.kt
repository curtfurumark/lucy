package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.coroutines.launch
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.composables.ItemChooser
import se.curtrune.lucy.screens.dev.DevEvent
import se.curtrune.lucy.screens.dev.DevState
import se.curtrune.lucy.web.LucindaApi

@Composable
fun DevScreen(modifier: Modifier = Modifier, state: DevState = DevState(), onEvent: (DevEvent) -> Unit = {}){
    //HolidaysTest(state = state, onEvent = onEvent)
    //NotificationTest(onEvent = onEvent)
    println("DevScreen")
    var message by remember { mutableStateOf("...") }
    val scope = rememberCoroutineScope()
    var showItemChooser by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "dev screen",
            fontSize = MaterialTheme.typography.headlineLarge.fontSize)
        Text(text = message)
        Button(onClick = { showItemChooser = true }) {
            Text(text = "show item chooser")
        }
        Button(onClick = {
            println("onClick()")
            try {
                val api = LucindaApi.create()
                scope.launch {
                    message = api.getAffirmation().affirmation
                }
            }catch (e: Exception){
                e.printStackTrace()
                message = e.message ?: "error unknown"
            }
        }) {
            Text(text = "create api")
        }
    }
    if( showItemChooser ){
        ItemChooser(
            onDismiss = {showItemChooser = false},
            {
                println("selected $it")
                showItemChooser = false
            }
        )
    }
}


@Composable
@PreviewLightDark
fun PreviewDevScreen(){
    LucyTheme {
        DevScreen()
    }
}