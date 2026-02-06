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
fun MedicineRemainingWithdrawalsCard(remainingWithdrawals: String, onRemainingWithdrawalsChanged: (String)-> Unit){
    var remWithdrawals by remember{
        mutableStateOf(remainingWithdrawals)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            value = remWithdrawals,
            onValueChange = {
                remWithdrawals = it
                onRemainingWithdrawalsChanged(it)
            },
            label = {
                Text(text = "remaining withdrawals")
            }
        )
    }
}