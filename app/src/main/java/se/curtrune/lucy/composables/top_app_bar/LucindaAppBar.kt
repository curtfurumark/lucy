package se.curtrune.lucy.composables.top_app_bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.composables.Search


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LucindaTopAppBar(mental: Mental, onEvent: (TopAppBarEvent)->Unit){
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(){
            Search(onSearch = {filter, everywhere->
                println("filter: $filter, everywhere $everywhere")
                onEvent(TopAppBarEvent.OnSearch(filter, everywhere))
            })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.clickable {
                    //onEvent("boost")
                },
                imageVector = Icons.Default.Star,
                contentDescription = "boost me"
            )
            //TopAppBar(title = { Text(text = "energy ${mental.energy}", color = Color.White) })
            Text(text = "hllo")
            Icon(
                modifier = Modifier.clickable {
                    //onEvent("panic")
                },
                imageVector = Icons.Default.Share,
                contentDescription = "panic"
            )
        }
    }
}
@Composable
@Preview(showBackground = true)
fun MyPreview(){
    LucindaTopAppBar(Mental()) {
        println("event $it")
    }
}