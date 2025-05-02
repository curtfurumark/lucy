package se.curtrune.lucy.activities.kotlin.composables.user_settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Categories(state: UserState, onEvent: (UserEvent)->Unit){
    //val scrollState = rememberScrollState()
    Card(modifier = Modifier.fillMaxWidth()){
        Column(modifier = Modifier.fillMaxWidth())
            //verticalScroll(state = scrollState))
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "categories", fontSize = 24.sp)
                Spacer(Modifier.weight(1f))
                Icon(
                    modifier = Modifier.clickable {
                        println("on add category click")
                     },
                    imageVector = Icons.Default.Add,
                    contentDescription = "add category"
                )
            }
            state.categories.forEach { category->
                Row() {
                    Text(
                        text = category,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete category",
                        modifier = Modifier.clickable {
                            onEvent(UserEvent.DeleteCategory(category))
                        })
                }
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
