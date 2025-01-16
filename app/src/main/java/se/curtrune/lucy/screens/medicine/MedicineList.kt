package se.curtrune.lucy.screens.medicine

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.MedicineContent

@Composable
fun MedicineList(state: MedicineState, onEvent:(MedicineEvent)->Unit){
    LazyColumn(modifier = Modifier.fillMaxSize()){
        items(state.items) {
            MedicineItem(it, onEvent = onEvent)
        }
    }
}


@Composable
fun DropdownItem(action: String, onClick: (String)->Unit){
    Text(text = action, fontSize = 18.sp, modifier = Modifier.clickable{
        onClick(action)
    }.padding(16.dp))
}