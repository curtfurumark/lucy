package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun DoctorCard(name: String, onNameChanged: (String) -> Unit){
    var name by remember {
        mutableStateOf(name)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                onNameChanged(it)
            },

        )

    }

}