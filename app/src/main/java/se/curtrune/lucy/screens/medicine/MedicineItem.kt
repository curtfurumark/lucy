package se.curtrune.lucy.screens.medicine

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.MedicineContent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MedicineItem(item: Item, onEvent: (MedicineEvent) -> Unit){
    val medicine = item.content as MedicineContent
    var showContextMenu by remember {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    val density = LocalDensity.current
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    Card(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .pointerInput(true) {
            detectTapGestures(
                onLongPress = {
                    showContextMenu = true
                    pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                },
                onTap = {
                    onEvent(MedicineEvent.Edit(item))
                }
            )
        }
        .onSizeChanged {
            itemHeight = with(density){it.height.toDp()}
        }
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
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight
            ),
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