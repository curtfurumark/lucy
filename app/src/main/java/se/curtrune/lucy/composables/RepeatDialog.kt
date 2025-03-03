package se.curtrune.lucy.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R
import se.curtrune.lucy.classes.Repeat
import se.curtrune.lucy.persist.RepeatItems
import se.curtrune.lucy.persist.Repeater
import se.curtrune.lucy.screens.medicine.composable.DropdownItem
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatDialog(
        onDismiss: ()->Unit, onConfirm:
        (Repeat)->Unit,
        repeatIn: Repeat?
) {
    val repeatableSpec: RepeatItems.BasicRepeat
    var showCustom by remember{
        mutableStateOf(false)
    }
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var showWeekDays by remember {
        mutableStateOf(false)
    }
    val repeat by remember {
        mutableStateOf(repeatIn?:Repeat())
    }
    var repeatUnit by remember{
        mutableStateOf(Repeater.Unit.DAY)
    }
    var isWeekDays by remember {
        mutableStateOf(false)
    }
    var isInfinite by remember {
        mutableStateOf(true)
    }
    var firstDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var lastDate by remember {
        mutableStateOf(LocalDate.now()) }

    //var repeater = Repeater
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), modifier   = Modifier
            .fillMaxWidth()
            .background(
                Color.Blue
            )){
            Column(modifier = Modifier.fillMaxWidth()
                .padding(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    Checkbox(checked = isInfinite, onCheckedChange =
                    {isInfinite = !isInfinite})
                    Text(text = "infinite")
                }
                Row(modifier = Modifier.fillMaxWidth()
                    .clickable {
                        showDatePicker = true
                    },
                    verticalAlignment = Alignment.CenterVertically ){
                    Text(text = stringResource(R.string.from, ""),
                        fontSize = 20.sp)
                    Text(text = LocalDate.now().toString())
                }
                AnimatedVisibility(visible = !isInfinite) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.to, ""),
                            fontSize = 20.sp
                        )
                        Text(text = LocalDate.now().toString())
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically ){
                    Checkbox(checked = repeatUnit == Repeater.Unit.DAY,
                        onCheckedChange = {
                            repeat.setPeriod(1, Repeat.Unit.DAY)
                            repeatUnit = Repeater.Unit.DAY
                        })
                    Text(text = stringResource(R.string.every_day))
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    Checkbox(checked = repeatUnit == Repeater.Unit.WEEK, onCheckedChange = {
                        repeat.setPeriod(1, Repeat.Unit.WEEK)
                        repeatUnit = Repeater.Unit.WEEK
                    })
                    Text(text = stringResource(R.string.every_week))
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    Checkbox(checked = repeatUnit == Repeater.Unit.MONTH, onCheckedChange = {
                        repeat.setPeriod(1, Repeat.Unit.MONTH)
                        repeatUnit = Repeater.Unit.MONTH
                    })
                    Text(text = stringResource(R.string.every_month))
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    Checkbox(checked = repeatUnit == Repeater.Unit.YEAR, onCheckedChange = {
                        repeat.setPeriod(1, Repeat.Unit.YEAR)
                        repeatUnit = Repeater.Unit.YEAR
                    })
                    Text(text = stringResource(R.string.every_year))
                }
                Text(text = stringResource(R.string.custom),
                    fontSize = 24.sp,
                    modifier = Modifier.clickable {
                        showCustom = !showCustom
                    })
                AnimatedVisibility(visible = showCustom){
                    //Text(text = "custom period")
                    var quantifier by remember {
                        mutableStateOf("1")
                    }
                    var unit by remember {
                        mutableStateOf("day")
                    }
                    var unitDropdownExpanded by remember {
                        mutableStateOf(false)
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text = "every")
                        TextField(
                            modifier = Modifier.width(60.dp),
                            value = quantifier, onValueChange = {
                                quantifier = it

                        })
                        Text(text = unit,
                            modifier = Modifier.clickable {
                                unitDropdownExpanded = !unitDropdownExpanded
                            })
                        DropdownMenu(expanded = unitDropdownExpanded, onDismissRequest = {
                            unitDropdownExpanded = false
                            println("dismissed")
                        } ){
                            val actions = listOf("day", "week", "month", "year")
                            actions.forEach { action->
                                DropdownItem(action = action, onClick = { name->
                                    println("action name: $name")
                                    unit = name
                                    unitDropdownExpanded = false
                                })
                            }

                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
/*                Text(text = "weekdays", fontSize = 24.sp,
                    modifier = Modifier.clickable {
                        showWeekDays = !showWeekDays
                    })
                AnimatedVisibility(visible = true) {
                    WeekDayChooser { dayOfWeek, selected ->
                        println("selected: ${dayOfWeek.name}")
                        if( selected) {
                            repeat.add(dayOfWeek)
                        }else{
                            repeat.remove(dayOfWeek)
                        }
                    }
                }*/
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = onDismiss) {
                        Text(text = stringResource(R.string.dismiss))
                    }
                    Button(onClick = { onConfirm(repeat)
                            }) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }
    if( showDatePicker) {
        DatePickerModal(onDismiss = {
            showDatePicker = false
        }, onDateSelected = { date ->
            println("date: $date")
        })
    }
}
@Composable
fun CustomRepeat(){


}
@Preview
@Composable
fun Preview(){
    se.curtrune.lucy.composables.RepeatDialog(onConfirm = {}, onDismiss = {}, repeatIn = null)
}
