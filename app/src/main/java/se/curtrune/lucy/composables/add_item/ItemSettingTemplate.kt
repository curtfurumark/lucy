package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
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

@Composable
fun ItemSettingTemplate(item: Item, onIsTemplate: (Boolean) -> Unit){
    var isTemplate by remember {
        mutableStateOf(item.isTemplate)
    }
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(2.dp)
        //.border(Dp.Hairline, color = Color.LightGray)
        .padding(start = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.is_template))
            Checkbox(checked = isTemplate, onCheckedChange = {
                isTemplate = !isTemplate
                onIsTemplate(isTemplate)
            })
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewItemSettingTemplate(){
    LucyTheme {
        ItemSettingTemplate(item = Item(), onIsTemplate = {})
    }
}