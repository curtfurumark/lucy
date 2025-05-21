package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.medicine.composable.DropdownItem

@Composable
fun ItemSettingCategory(item: Item?){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(R.string.category))
        if (item != null) {
            Text(text = item.category)
        }
    }
}
@Composable
fun ItemSettingCategory(item: Item, categories: List<String>, onEvent: (String) -> Unit){
    var category by remember {
        mutableStateOf(item.category)
    }
    var categoryListExpanded by remember {
        mutableStateOf(false)
    }
    val hasCategory = item.category.isNotBlank()
    Card(modifier = Modifier.fillMaxWidth()
        .clickable {categoryListExpanded = !categoryListExpanded }
        .padding(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if( hasCategory){
                Text(text = category)
            }else {
                Text(text = stringResource(R.string.category))
            }
            //Text(text = category)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "chose category")
            DropdownMenu(expanded = categoryListExpanded, onDismissRequest = {
                categoryListExpanded = false
            }) {
                categories.forEach {
                    DropdownItem(action =it) { cat ->
                        category = cat
                        categoryListExpanded = false
                        onEvent(category)
                    }
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
        ItemSettingCategory(item = item, categories = categories,onEvent = {})
    }
}