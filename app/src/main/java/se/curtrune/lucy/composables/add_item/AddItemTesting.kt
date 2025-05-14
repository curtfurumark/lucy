package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.top_app_bar.ItemSettingDate

@Composable
fun AddItemTesting(){
    LazyRow(){
        item {Text(text = "hello")}


    }
}



@Composable
@PreviewLightDark
fun PreviewAddItemTesting(){
    LucyTheme {
        AddItemTesting()
    }
}

fun getItems(item: Item):List<Composable>{
    val composables = mutableListOf<Composable>()
    //composables.add(ItemSettingDate(item,
    return composables
}