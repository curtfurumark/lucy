package se.curtrune.lucy.screens.medicine.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.screens.medicine.MedicineEvent
import se.curtrune.lucy.screens.medicine.MedicineState

@Composable
fun MedicineList(state: MedicineState, onEvent:(MedicineEvent)->Unit){
    LazyColumn(modifier = Modifier.fillMaxSize()){
        items(state.items) {
            MedicineItem(it, onEvent = onEvent)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}


@Composable
fun DropdownItem(action: String, onClick: (String)->Unit){
    Text(text = action, fontSize = 18.sp, modifier = Modifier.clickable{
        onClick(action)
    }.padding(16.dp))
}