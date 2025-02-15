package se.curtrune.lucy.composables.top_app_bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.screens.main.MainState

@Composable
fun LucindaTopAppBar(onEvent: (TopAppBarEvent)->Unit){
    Column(modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier.fillMaxWidth()
            .padding(4.dp)){
            Icon(imageVector = Icons.Default.Menu, contentDescription = "main menu",
                modifier = Modifier.clickable {

                })
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.Search, contentDescription = "search",
                modifier = Modifier.clickable {

                })
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "day calendar",
                modifier =  Modifier.clickable {
                    onEvent(TopAppBarEvent.DayCalendar)

                })
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "action menu",
                modifier = Modifier.clickable {
                    onEvent(TopAppBarEvent.Menu)
                }
            )
        }
        Row(){
            LucindaControls(state = MainState(), onEvent = onEvent)
        }
    }
}