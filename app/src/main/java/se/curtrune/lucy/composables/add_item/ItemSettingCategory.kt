package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.medicine.composable.DropdownItem

/*@Composable
fun ItemSettingCategory(item: Item, onCategoryChanged: (String) -> Unit){
    var category by remember {
        mutableStateOf(item.category)
    }
    var expandCategoryList by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable{
                println("click")
                //onCategoryChanged(category)
            }
    ) {
        Text(text = stringResource(R.string.category))
        category.let {
            if (it.isNotBlank()) {
                Text(text = category)
            }else{
                Text(text = "click to set category")
            }
        }
    }
}*/
@Composable
fun ItemSettingCategory(
    modifier: Modifier = Modifier,
    item: Item, categories:
    List<String>, onCategoryChanged: (String) -> Unit,
    onAddNewCategory: (String) -> Unit){
    var category by remember {
        mutableStateOf(item.category)
    }
    var categoryListExpanded by remember {
        mutableStateOf(false)
    }
    var showAddCategoryDialog by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier.fillMaxWidth()
        .clickable {categoryListExpanded = !categoryListExpanded })
    {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Text(text = stringResource(R.string.category))
            Text(
                modifier =  Modifier.padding(8.dp),
                text = category.ifBlank { "click to set category" })
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "chose category")
            DropdownMenu(expanded = categoryListExpanded, onDismissRequest = {
                categoryListExpanded = false
            }) {
                DropdownItem(action = "add new category"){
                    println("add category")
                    showAddCategoryDialog = true
                    categoryListExpanded = false
                }
                categories.forEach {
                    DropdownItem(action =it) { cat ->
                        category = cat
                        categoryListExpanded = false
                        onCategoryChanged(category)
                    }
                }
            }
        }
    }
    if( showAddCategoryDialog) {
        println("showAddCategoryDialog")
        AddCategoryDialog(onDismiss = {
            showAddCategoryDialog = false
        }, onCategoryAdded = {
            category = it
            onAddNewCategory(it)
            showAddCategoryDialog = false
        })

    }
}
@Composable
fun AddCategoryDialog(onDismiss: () -> Unit, onCategoryAdded: (String) -> Unit) {
    var category by remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(text = "add category")
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = category,
                onValueChange = {category = it})
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Button(onClick = { onDismiss() }) {
                    Text(text = "cancel")

                }
                Button(onClick = {
                    onCategoryAdded(category)
                }) {
                    Text(text = "add category")
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewItemSettingCategory(){
    LucyTheme {
        val item = Item()
        item.category = "dev"
        val categories = listOf("dev", "bass", "health")
        ItemSettingCategory(
            item = item, categories = categories,
            onCategoryChanged = {},
            onAddNewCategory = {})
    }
}