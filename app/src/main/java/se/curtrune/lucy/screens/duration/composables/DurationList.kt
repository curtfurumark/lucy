package se.curtrune.lucy.screens.duration.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.screens.duration.DurationEvent
import se.curtrune.lucy.screens.duration.DurationState
import se.curtrune.lucy.statistics.CategoryListable

@Composable
fun DurationList(state: DurationState, onEvent: (DurationEvent)-> Unit){
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(state.items){ item->
            DurationItem(item as CategoryListable)
            Spacer(modifier = Modifier.height(2.dp))
        }
    }

}