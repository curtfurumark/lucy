package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.web.LucindaApi

@Composable
fun MessageKtorTest(){
    var reply by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    Card(modifier = Modifier.fillMaxWidth()){
        Column(modifier = Modifier.fillMaxWidth()){
            Text(text = reply)
            Button(onClick = {
                println("onClick")
                scope.launch {
                    val messages = LucindaApi.create().getMessages()
                    messages.forEach { message ->
                        println(message.toString())
                    }
                }
            }){
                Text(text ="hello")
            }
        }
    }
}

@Composable
@Preview
fun PreviewMessageKtorTest(){
    LucyTheme {
        MessageKtorTest()
    }
}