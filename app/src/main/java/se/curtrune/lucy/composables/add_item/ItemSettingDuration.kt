package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import se.curtrune.lucy.R
import se.curtrune.lucy.classes.ItemDuration
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.medicine.composable.DropdownItem

@Composable
fun ItemSettingDuration(item: Item, onEvent: (ItemDuration)->Unit){
    var showDropdown by remember {
        mutableStateOf(false)
    }
    var itemDuration by remember {
        mutableStateOf(ItemDuration())
    }
    var itemDurationType by remember {
        mutableStateOf(item.itemDuration?.type?: ItemDuration.Type.SECONDS)
    }
    Box(
        modifier = Modifier.fillMaxWidth()
            .border(Dp.Hairline, color = Color.LightGray)
            .clickable {
                showDropdown = !showDropdown
            }){
        Column(modifier = Modifier.fillMaxWidth()){
            Text(text = stringResource(R.string.duration))
            Text(text = itemDurationType.name)
            DropdownMenu(expanded = showDropdown, onDismissRequest = {
                showDropdown = false
            }) {
                ItemDuration.Type.entries.forEach { durationType->
                    DropdownItem(action = durationType.name) {
                        itemDuration.type = durationType
                        itemDurationType = durationType
                        showDropdown = false
                        onEvent(itemDuration)
                    }
                }
            }
        }

    }
}