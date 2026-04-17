package se.curtrune.lucy.screens.my_manual.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.screens.medicine.composable.MedicineCard
import se.curtrune.lucy.screens.medicine.composable.MedicineDialog
import se.curtrune.lucy.screens.my_manual.MyManualGuideEvent
import se.curtrune.lucy.screens.my_manual.MyManualGuideState
import se.curtrune.lucy.screens.my_manual.MyManualGuideViewmodel

@Composable
fun PageTwo(state: MyManualGuideState, onEvent: (MyManualGuideEvent)->Unit){
    var showAddMedicine by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showAddMedicine = true
            }) {
                Text(text = "add")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
        ) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "page two, medicines"
                )
            }
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
            items(state.medicines){
                MedicineCard(it, onEvent = {})
                Spacer(modifier = Modifier.height(2.dp))
            }

            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { onEvent(MyManualGuideEvent.OnBackPressed) }) {
                        Text(text = "back")
                    }
                    Button(onClick = { onEvent(MyManualGuideEvent.OnPageTwoCompleted) }) {
                        Text(text = "next")
                    }
                }
            }
        }
    }
    if( showAddMedicine){
        MedicineDialog(
            onDismiss = {
                showAddMedicine = false},
            onConfirm = {
                showAddMedicine = false
                onEvent(MyManualGuideEvent.OnAddMedicine(it))
            }
        )

    }
}


@Composable
@Preview
fun PreviewPageTwo(){
    PageTwo(state = MyManualGuideState(), onEvent = {})


}