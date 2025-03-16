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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import se.curtrune.lucy.web.LucindaApi

@Composable
fun MessageKtorTest(){
    var reply by remember {
        mutableStateOf("")
    }
    LaunchedEffect(true) {
        val messages = LucindaApi.create().getMessages()
        messages.forEach{ message->
            println(message.toString())
        }
    }
    Card(modifier = Modifier.fillMaxWidth()){
        Column(modifier = Modifier.fillMaxWidth()){
            Text(text = reply)
            Button(onClick = {
                println("onClick")
            }){
                Text(text ="hello")
            }
        }
    }
}