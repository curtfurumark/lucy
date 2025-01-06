package se.curtrune.lucy.activities.kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.composables.MedicineDialog
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.activities.kotlin.viewmodels.MedicineViewModel
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.MedicineContent

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