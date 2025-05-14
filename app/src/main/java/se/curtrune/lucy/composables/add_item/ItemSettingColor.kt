package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.ColorPicker

@Composable
fun ItemSettingColor(item: Item, onColor: (Color) -> Unit){
    var showColorDialog by remember {
        mutableStateOf(false)
    }
    Card(modifier = Modifier.fillMaxWidth()
        //.border(Dp.Hairline, color = Color.LightGray)
        .padding(8.dp)
        //.background(color = Color(item.color))
        .clickable {
            showColorDialog = true
        }) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .background(color = Color(item.color))) {
            Text(
                text = stringResource(R.string.color))
        }
    }
    if( showColorDialog) {
        ColorPicker(dismiss = {
            showColorDialog = false
        }, onColor = { color ->
            item.color = color.toArgb()
            showColorDialog = false
        })
    }
}

@Composable
@PreviewLightDark
fun PreviewItemSettingColor(){
    LucyTheme {
        val item = Item("item with color")
        item.color = Color.Red.toArgb()
        ItemSettingColor(item = item, onColor = {})

    }
}