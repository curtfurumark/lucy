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
fun AffirmationTest(){
    val scope = rememberCoroutineScope()
    var affirmation by remember{
        mutableStateOf("")
    }
    Card(modifier = Modifier.fillMaxWidth()){
        Column(modifier = Modifier.fillMaxWidth()){
           Text(text = affirmation)
            Button(onClick = {
                scope.launch {
                    println("onClick in coroutine scope")
                    val affirmationObject = LucindaApi.create().getAffirmation()
                    println("after api call")
                    affirmation = affirmationObject.affirmation
                }
            }) {
                Text(text = "get affirmed")
            }
        }
    }
}