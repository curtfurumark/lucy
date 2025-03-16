package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import se.curtrune.lucy.web.LucindaApi

@Composable
fun GetQuoteKtor() {
    var quote by remember {
        mutableStateOf("")
    }
    var author by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()

    Card(modifier = Modifier.fillMaxWidth()
        .padding(4.dp)) {
        Column() {
            Text(text = quote)
            Text(text = author)
            Button (onClick = {
                scope.launch {
                    val quotes = LucindaApi.create().getQuotes()
                    if( quotes.isNotEmpty()){
                        quote = quotes[0].q
                        author = quotes[0].a
                    }else{
                        println("get quotes returned empty list? wtf")
                    }
                }
            }){
                Text(text = "get quote")
            }
        }
    }
}