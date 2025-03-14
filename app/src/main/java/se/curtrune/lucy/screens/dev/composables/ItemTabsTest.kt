package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.dev.DevEvent
import se.curtrune.lucy.screens.dev.DevState
import java.time.LocalDate

@Composable
fun ItemsTabsTest(state: DevState, onEvent: (DevEvent) -> Unit){
    var itemHeading by remember{
        mutableStateOf("")
    }

    Column() {
        TextField(
            value = itemHeading,
            onValueChange ={ itemHeading = it })
        Button(onClick = {
            onEvent(DevEvent.AddTab(itemHeading))
            itemHeading = ""
        }){
            Text(text = "add item to tabs")
        }
        LazyRow() {
            items(state.tabs){ tab ->
                ItemTabComposable(tab = tab) {
                    println("on tab click")
                }
            }
        }
    }

    //}
}
@Composable
fun ItemTabComposable(tab: MyTab, onEvent: (DevEvent)->Unit){
    Box(
        modifier = Modifier.border(BorderStroke(1.dp, color = Color.Yellow))
            .padding(4.dp)
            .clickable {
                println("on click ")
            }) {
        Text(text = tab.getHeading())
    }
}


sealed interface MyTab{
    fun getHeading():String
    data class DateTab(val date: LocalDate): MyTab {
        override fun getHeading(): String {
            return date.toString()
        }
    }

    data class ItemTab(val item: Item): MyTab {
        override fun getHeading(): String {
            return item.heading
        }
}

@Composable
@Preview(showBackground = true)
fun PreviewTabs(){
    //ItemTabComposable() { }
}}