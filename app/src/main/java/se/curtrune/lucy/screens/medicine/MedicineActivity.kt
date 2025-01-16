package se.curtrune.lucy.screens.medicine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme

class MedicineActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val medicineViewModel = viewModel<MedicineViewModel>(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return MedicineViewModel(applicationContext) as T
                    }
                }
            )
            val state = medicineViewModel.state.collectAsState()
            if (state.value.showAddMedicineDialog) {
                MedicineDialog(onDismiss = {
                    medicineViewModel.onEvent(MedicineEvent.ShowAddMedicineDialog(false))
                }, onConfirm = { item ->
                    medicineViewModel.onEvent(MedicineEvent.Insert(item))
                    medicineViewModel.onEvent(MedicineEvent.ShowAddMedicineDialog(false))
                })
            }
            if (state.value.errorMessage.isNotEmpty()) {
                println("show error dialog")
            }
            if (state.value.message.isNotEmpty()) {
                println("show message dialog")
            }
            LucyTheme {
                Scaffold(floatingActionButton = {
                    AddButton(onAddClick = {
                        medicineViewModel.onEvent(MedicineEvent.ShowAddMedicineDialog(true))
                    })
                }, topBar = { MyTopBar() }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MedicineList(state = state.value, onEvent = { event ->
                            medicineViewModel.onEvent(event)
                        })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(){
    TopAppBar(title = { Text(text = "Medicine list")})
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



