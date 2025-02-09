package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import se.curtrune.lucy.screens.affirmations.RetrofitInstance

@Composable
fun GetQuote() {
    var quote by remember {
        mutableStateOf("")
    }
    var getQuote by remember{
        mutableStateOf(false)
    }
    LaunchedEffect(getQuote) {
        val quotes = RetrofitInstance.quoteApi.getRandomQuotes()
        quote = quotes[0].q
        println(quote)

    }
    Card(modifier = Modifier.fillMaxWidth()
        .padding(4.dp)) {
        Text(text = "click to refresh", fontSize = 24.sp,
            modifier = Modifier.clickable{
                getQuote = !getQuote
            })
        Text(text = quote)
    }
}