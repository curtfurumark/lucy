package se.curtrune.lucy.screens.dev.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import se.curtrune.lucy.classes.item.Repeat

@Composable
fun RepeatTest() {
    var showRepeatDialog by remember {
        mutableStateOf(false)
    }
    var repeat by remember {
        mutableStateOf(Repeat())
    }
    //var items = remember { mutableStateListOf<Item>() }
/*    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "repeater test")
        Text(text = repeat?.toString() ?: "no repeat set")
        Text(
            text = "click to show repeat dialog",
            modifier = Modifier.clickable {
                showRepeatDialog = !showRepeatDialog
            })

        Row(modifier = Modifier.fillMaxWidth()){
            Button(onClick = {
                val template =
                    Item("temp template")
                repeat?.template = template
                val items = repeat?.let { Repeater.getItems(it) }
                items?.forEach{ item->
                    println(item.toString())
                }
            }) {
                Text(text = "run test")
            }
        }*/
    }
/*    if( showRepeatDialog){
        RepeatDialog(onDismiss = {
            showRepeatDialog = false
        }
        , onConfirm = {
                println("onConfirm click")
                repeat = it
                showRepeatDialog = false
            })
    }*/
//}