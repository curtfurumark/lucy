package se.curtrune.lucy.screens.my_day.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.composables.Field
import se.curtrune.lucy.screens.my_day.MyDayEvent
import se.curtrune.lucy.screens.my_day.MyDayState

@Composable
fun MentalScreen(state: MyDayState, onEvent: (MyDayEvent)->Unit){
    var currentField by remember{
        mutableStateOf(Field.ENERGY)
    }
    Surface(modifier = Modifier.fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            MentalViewer(mental = state.mental)
            MyDayHeader(state = state, onEvent = onEvent)
            MyDayItemList(state = state, onEvent = onEvent)
        }
    }
}
