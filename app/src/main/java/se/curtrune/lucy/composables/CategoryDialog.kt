package se.curtrune.lucy.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.dev.ui.theme.LucyTheme
import se.curtrune.lucy.app.UserPrefs
import se.curtrune.lucy.screens.medicine.composable.DropdownItem


@Composable
fun CategoryDialog(category: String? ,dismiss: ()->Unit, onCategory: (String)->Unit){
    var mutableCategory by remember {
        mutableStateOf(category)
    }
    var showDropDown by remember {
        mutableStateOf(false)
    }
    //User.getCategories()
    val categories = LucindaApplication.userSettings.getCategories()
    Dialog(onDismissRequest = {
        dismiss()
    }){
        Card(modifier = Modifier.fillMaxWidth()){
            Column(modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
                .clickable { showDropDown = !showDropDown },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                Text(text = "category", fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))
                mutableCategory?.let { Text(text = it) }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Button(onClick = {
                        dismiss()
                    }){
                        Text(text = stringResource(R.string.dismiss))
                    }
                    Button(onClick = {
                        mutableCategory?.let { onCategory(it) }
                    }){
                        Text(text = stringResource(R.string.ok))
                    }
                }
                if(showDropDown){
                    println("show drop down")
                    DropdownMenu(expanded = showDropDown, onDismissRequest = {
                        showDropDown = false
                    }) {
                        categories.forEach { category ->
                            DropdownItem(action = category, onClick = {
                                onCategory(it)
                                mutableCategory = it
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewCategoryDialog(){
    LucyTheme {
        CategoryDialog(category = "", dismiss = {}, onCategory = {

        })
    }
}