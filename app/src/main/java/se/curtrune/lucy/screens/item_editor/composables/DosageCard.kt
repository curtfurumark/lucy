package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun DosageCard(dosage: String, onDosageChanged: (String) -> Unit){
    var dosage by remember {
        mutableStateOf(dosage)
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dosage,
            onValueChange = {
                dosage = it
                onDosageChanged(it)
            },
            label = {
                Text(text = "dosage")
            }
        )
    }
}