package se.curtrune.lucy.composables.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R

@Composable
fun AddCategoryDialog(dismiss: ()->Unit, onCategory: (String)->Unit){
    var category by remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = {}) {
        Card(modifier = Modifier.fillMaxWidth()){
            Column() {
                Row(){
                    OutlinedTextField(
                        value = category,
                        onValueChange = {category = it}
                    )
                }
                Row(){
                    Button(onClick = {
                        dismiss()
                    }){
                        Text(text = stringResource(R.string.dismiss))
                    }
                    Button(onClick = {
                        onCategory(category)
                    }){
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}