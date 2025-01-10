package se.curtrune.lucy.activities.kotlin.medicine

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MedicineItem(item: Item, onEvent: (MedicineEvent) -> Unit){
    val medicine = item.content as MedicineContent
    var showContextMenu by remember {
        mutableStateOf(false)
    }
    if( showContextMenu){
        Toast.makeText(LocalContext.current, "I AM NOT A CONTEXT MENU", Toast.LENGTH_LONG).show()
    }
    Card(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .combinedClickable(
            onClick = {
                onEvent(MedicineEvent.Edit(item))
            },
            onLongClick = {
                showContextMenu = true
            }
        )
        , shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
                .padding(8.dp)
        ) {
            Text(text = medicine.name.uppercase(), fontSize = 20.sp)
            Text(text = medicine.dosage)
            Text(text = medicine.doctor)
            Text(text = medicine.bipacksedel)
            Text(text = "uttag kvar")
            Text(text = "giltigt till och med")
        }
        DropdownMenu(
            expanded = showContextMenu,
            onDismissRequest ={
                showContextMenu = false
            } ) {
            listOf("biverkning", "förpackning").forEach {
                DropdownItem(it, onClick = {
                   println("action: $it")
                    showContextMenu = false
                })
            }

        }
    }
}
@Composable
fun DropdownItem(action: String, onClick: (String)->Unit){
    Text(text = action, modifier = Modifier.clickable{
        onClick(action)
    })
}