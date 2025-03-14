package se.curtrune.lucy.screens.medicine.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R
import se.curtrune.lucy.composables.TimePickerDialog
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.MedicineContent
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineDialog(onDismiss: () -> Unit, onConfirm: (Item)-> Unit){
    var name by remember{
        mutableStateOf("")
    }
    var dosage by remember {
        mutableStateOf("")
    }
    var doctor by remember{
        mutableStateOf("")
    }
    var strength by remember {
        mutableStateOf("")
    }
    var beredningsform by remember {
        mutableStateOf("")
    }
    var medicineTimes =  remember{
        //mutableStateOf(emptyArray())
        mutableStateListOf<LocalTime>()
    }
    var bipacksedel by remember {
        mutableStateOf("")
    }
    var showTimePicker by remember {
        mutableStateOf(false)
    }
    var quantity by remember {
        mutableStateOf("")
    }
    if(showTimePicker){
        TimePickerDialog(onDismiss = { showTimePicker = false }, onConfirm = {
            var targetTime = LocalTime.of(it.hour, it.minute)
            medicineTimes.add(targetTime)
            showTimePicker = false
        })
    }
    Dialog(onDismissRequest = {onDismiss() }) {
        Card(modifier = Modifier
            .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(R.string.add_medicine), fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = { Text(stringResource(R.string.medicine_name)) }
                )
                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder ={
                        Text(text = stringResource(R.string.dosage))
                    }
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder ={
                        Text(text = stringResource(R.string.quantity))
                    }
                )

                OutlinedTextField(
                    value = doctor  ,
                    onValueChange = { doctor = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder ={
                        Text(text = stringResource(R.string.doctor))
                    }
                )
/*                OutlinedTextField(
                    value = bipacksedel  ,
                    onValueChange = { bipacksedel = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder ={
                        Text(text = "bipacksedel")
                    }
                )*/
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text("times whatever", fontSize = 24.sp)
                    Icon(imageVector = Icons.Default.Add, contentDescription = "add time", modifier = Modifier.clickable {
                        println("add a time")
                        showTimePicker = true
                    })
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    medicineTimes.forEach(){
                        Text(text =  it.toString())
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = { onDismiss() }) {
                        Text(text = stringResource(R.string.dismiss))
                    }
                    Button(onClick = {
                        var item = Item()
                        var medicineContent = MedicineContent()
                        medicineContent.name = name
                        medicineContent.dosage = dosage
                        medicineContent.doctor = doctor
                        medicineContent.bipacksedel = bipacksedel
                        item.heading = name
                        item.content = medicineContent
                        onConfirm(item)
                    }) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}