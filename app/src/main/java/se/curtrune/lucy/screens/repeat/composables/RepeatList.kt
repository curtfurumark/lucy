package se.curtrune.lucy.screens.repeat.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.item.Repeat

@Composable
fun RepeatList(repeats: List<Repeat>){
    LazyColumn(modifier = Modifier.fillMaxWidth()){
        items(repeats) { repeat->
            RepeatInfo(repeat = repeat)
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}