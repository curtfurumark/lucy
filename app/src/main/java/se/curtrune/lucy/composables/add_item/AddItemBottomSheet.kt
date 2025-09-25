package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.app.LucindaApplication
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemBottomSheet(
        defaultItemSettings: DefaultItemSettings,
        onDismiss: () -> Unit,
        onSave: (Item) -> Unit
    ){
    println("AddItemBottomSheet()")
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()
    if( defaultItemSettings.parent == null){
        println("parent is null")
    }
    var heading by remember {
        mutableStateOf("") }
    val item by remember {
        mutableStateOf(defaultItemSettings.item)
    }

    LaunchedEffect(Unit) {
        item.category = defaultItemSettings.parent?.category?: "<no category>"
        println("launched effect, category: ${item.category}")
    }

    ModalBottomSheet(
        onDismissRequest = { /* Handle dismissal */ },
        sheetState = bottomSheetState
    ){
        Column(
            modifier = Modifier.fillMaxWidth()) {
            val parentHeading = defaultItemSettings.parent?.heading?: "parent is null"

            Row(modifier = Modifier.fillMaxWidth()){
                Text(text = parentHeading)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = heading,
                    onValueChange = {
                        heading = it
                        item.heading = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.heading)) },
                    supportingText = {
                        if (heading.isBlank()) {
                            Text(
                                text = "heading required",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.SpaceEvenly ,
                verticalAlignment = Alignment.CenterVertically){
                ItemSettingDate(item , onDateChanged = { date->
                    item.targetDate = date
                })
                Spacer(modifier = Modifier.width(8.dp))
                ItemSettingTime(item = item, onTimeChanged = {
                    item.targetTime = it
                })
                Spacer(modifier = Modifier.width(8.dp))
                ItemSettingRepeat(item = item, onRepeatEvent = {
                    repeat->  item.repeat = repeat})
                Spacer(modifier = Modifier.width(8.dp))
                ItemSettingNotification(item = item, onNotication = {
                    notification-> item.notification = notification
                })
                Spacer(modifier = Modifier.width(8.dp))
                val categories = LucindaApplication.appModule.userSettings.categories
                ItemSettingCategory(item = item, categories = categories, onCategoryChanged = {
                    category-> item.category = category
                } )
                Spacer(modifier = Modifier.width(8.dp))
                ItemSettingAppointment(item = item, onEvent = {
                    item.setIsAppointment(it)
                })
                ItemSettingTemplate(item = item, onIsTemplate = { isTemplate ->
                    item.setIsTemplate(isTemplate)
                })
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        onDismiss()
                    }
                ){
                    Text(text = stringResource(R.string.dismiss))
                }
                Button(
                    onClick = { onSave(item) }){
                    Text(text = stringResource(R.string.add))
                }
            }
        }
    }
}

data class DefaultItemSettings(
    //var targetDate: LocalDate = LocalDate.now(),
    //val targetTime: LocalTime =  LocalTime.of(0, 0, 0),
    //val isTemplate: Boolean = false,
    //var isCalendarItem: Boolean = false,
    val parent: Item? = null,
    var item: Item = Item(),
    val isAppointment: Boolean = false,
    val isEvent: Boolean = true,
    val categories: List<String> = emptyList()

)
@Composable
@Preview
@PreviewLightDark
fun PreviewBottomSheetAddItem(){
    LucyTheme {
        AddItemBottomSheet(defaultItemSettings = DefaultItemSettings(), onDismiss = {}, onSave = {})
    }

}