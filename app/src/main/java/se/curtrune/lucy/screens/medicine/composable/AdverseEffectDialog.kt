package se.curtrune.lucy.screens.medicine.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog

@Composable
fun AdverseEffectDialog(onDismiss: ()->Unit){
    Dialog(onDismissRequest = onDismiss){
    }
        Column(modifier = Modifier.fillMaxWidth()){
            Text(text = "hello adverse effects")
        }
    }