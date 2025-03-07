package se.curtrune.lucy.activities.kotlin.composables.user_settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.user_settings.UserEvent
import se.curtrune.lucy.screens.user_settings.UserState

@Composable
fun Categories(state: UserState, onEvent: (UserEvent)->Unit){
    Card(modifier = Modifier.fillMaxWidth()){
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "categories", fontSize = 24.sp)
                Icon(
                    modifier = Modifier.clickable {
                        println("on add category click")
                    },
                    imageVector = Icons.Default.Add,
                    contentDescription = "add category"
                )
            }
            state.categories.forEach { category->
                Text(text = category, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun Preview(){
    LucyTheme {
        val state = UserState()
        state.categories = listOf("work", "play", "art", "music")
        Categories(state = state) { }
    }
}
fun getCategories(): List<String>{
    return listOf("home", "work", "play", "household","dev")
}