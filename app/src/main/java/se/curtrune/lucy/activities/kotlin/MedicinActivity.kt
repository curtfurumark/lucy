package se.curtrune.lucy.activities.kotlin

import android.os.Bundle
import android.widget.TimePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.composables.TimePickerDialog
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.activities.kotlin.viewmodels.MedicineViewModel
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.MedicineContent
import java.time.LocalTime
import java.util.Locale

class MedicinActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showDialog by remember{
                mutableStateOf(false)
            }
            val medicineViewModel = viewModel<MedicineViewModel>(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return MedicineViewModel(applicationContext) as T
                    }
                }
            )
            LucyTheme {
                Scaffold(floatingActionButton = { AddButton(onAddClick = {
                    println("setting showDialog to true")
                    showDialog = true
                })}, topBar = { MyTopBar()}) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MedicineList(medicineViewModel.getMedicineList())
                        if( showDialog){
                            MedicineDialog(
                                onDismiss = {
                                    showDialog = false
                                    //println("onDismiss()");
                                }, onConfirm = {medicine ->
                                    println("onConfirm(), medicine name ${medicine.name}")
                                    medicineViewModel.addMedicine(medicine)
                                    showDialog = false
                                }
                            )
                            }
                        }
                    }
                }
            }
        }
    }
@Composable
fun MyBottomAppBar(){
    BottomAppBar {
        Text(text = "my bottom app bar")
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(){
    TopAppBar(title = { Text(text = "MedicinLista")})
}

@Composable
fun AddButton(onAddClick: ()->Unit){
    FloatingActionButton(
        //modifier = Modifier.padding(16.dp).align(Alignment.Bottom),
        onClick = {
            onAddClick()
            println("add button clicked")
        },
    ) {
        Icon(Icons.Filled.Add, "add medicine item")
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineDialog(onDismiss: () -> Unit, onConfirm: (MedicineContent)-> Unit){
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
            shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(1f),
                verticalArrangement =Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "add medicine", fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder ={
                        Text(text = "name")
                    }
                )
                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder ={
                        Text(text = "dosage")
                    }
                )
                OutlinedTextField(
                    value = doctor  ,
                    onValueChange = { doctor = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder ={
                        Text(text = "doctor")
                    }
                )
                OutlinedTextField(
                    value = bipacksedel  ,
                    onValueChange = { bipacksedel = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder ={
                        Text(text = "bipacksedel")
                    }
                )
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
                        Text(text = "cancel")
                    }
                    Button(onClick = {
                        var medicineContent = MedicineContent()
                        medicineContent.name = name
                        medicineContent.dosage = dosage
                        medicineContent.doctor = doctor
                        medicineContent.bipacksedel = bipacksedel
                        onConfirm(medicineContent)
                    }) {
                        Text(text = "add")
                    }
                }
            }
        }
    }
}
@Composable
fun MedicineList(medicineList: List<Item>){
    LazyColumn(modifier = Modifier.fillMaxSize()){
        items(medicineList) {
            MedicineItem(it)
        }
    }
}
@Composable
fun MedicineItem(item: Item){
    val medicine = item.content as MedicineContent
    Card(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .clickable {
            println("on medicine click $medicine")
        }, shape = RoundedCornerShape(8.dp)) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    LucyTheme {
        MedicineDialog(onDismiss = {
            println("onDismiss()")
        }, onConfirm = {
            println("onConfirm()")
        })
    }
}