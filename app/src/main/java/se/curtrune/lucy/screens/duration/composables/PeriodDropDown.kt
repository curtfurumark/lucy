package se.curtrune.lucy.screens.duration.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.screens.duration.DurationEvent
import se.curtrune.lucy.screens.duration.DurationPeriod
import se.curtrune.lucy.screens.medicine.DropdownItem

@Composable
fun PeriodDropDown(onEvent: (DurationEvent)->Unit){
/*    var currentPeriod by remember {
        mutableStateOf(DurationPeriod.)
    }*/
    var expanded by remember {
        mutableStateOf(false)
    }
    var currentPeriod by remember {
        mutableStateOf(DurationPeriod.DATE.name)
    }
    Column(modifier = Modifier.fillMaxWidth()){
        Text(
            text = currentPeriod,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.clickable {
                expanded = true
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false }
            , containerColor = Color.Gray
        ){
            DurationPeriod.entries.forEach { period->
                DropdownItem(action = period.name, onClick = {
                    onEvent(DurationEvent.SetPeriod(period))
                    currentPeriod = period.name
                    expanded = false
                } )
            }
        }
    }
}

@Composable
@Preview
fun Preview(){
    PeriodDropDown(onEvent = {
        println("onEvent")
    })
}