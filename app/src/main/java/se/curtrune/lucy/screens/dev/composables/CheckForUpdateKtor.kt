package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import se.curtrune.lucy.web.LucindaApi

@Composable
fun  CheckForUpdateKtor(){
    val scope = rememberCoroutineScope()
    var result by remember {
        mutableStateOf("check for update")
    }
    Card(modifier = Modifier.fillMaxWidth()){
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = result)
            Button(onClick = {
                scope.launch {
                    try {
                        val versionInfo = LucindaApi.create().getUpdateAvailable()
                        result = versionInfo.toString()
                    }catch (exception: Exception){
                        result = exception.message.toString()
                    }
                }
            }) {
                Text(text = "check for update")
            }
        }

    }
}