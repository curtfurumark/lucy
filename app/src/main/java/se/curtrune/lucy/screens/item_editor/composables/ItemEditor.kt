package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.classes.MedicineContent
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.add_item.ItemSettingAppointment
import se.curtrune.lucy.composables.add_item.ItemSettingCalendar
import se.curtrune.lucy.composables.add_item.ItemSettingCategory
import se.curtrune.lucy.composables.add_item.ItemSettingDate
import se.curtrune.lucy.composables.add_item.ItemSettingPriority
import se.curtrune.lucy.composables.add_item.ItemSettingState
import se.curtrune.lucy.composables.add_item.ItemSettingTemplate
import se.curtrune.lucy.composables.add_item.ItemSettingTime
import se.curtrune.lucy.screens.item_editor.ItemEvent

@Composable
fun ItemEditor(
        modifier: Modifier = Modifier,
        item: Item,
        categories: List<String> = listOf(),
        onEvent: (ItemEvent) -> Unit,
        onSave: (Item) -> Unit) {
    var heading by remember {
        mutableStateOf(item.heading)
    }
    var comment by remember {
        mutableStateOf(item.comment)
    }
    var energy by remember {
        mutableStateOf(5f)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton (onClick = {
            onSave(item)
        }){
            Icon(imageVector = androidx.compose.material.icons.Icons.Default.Check, contentDescription = "save")
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = heading,
            label = { Text(text = "heading") },
            onValueChange = {
                heading = it
                item.heading = it
                onEvent(ItemEvent.Update(item))
            })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = comment,
            onValueChange = {
                comment = it
                item.comment = it
                onEvent(ItemEvent.Update(item))
            },
            label = {Text(text = "comment")})
        Spacer(modifier = Modifier.height(8.dp))

        ItemSettingDate(item = item, onDateChanged = {
            item.targetDate = it
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(4.dp))
        ItemSettingTime(item = item, onTimeChanged = {
            item.targetTime = it
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(4.dp))
        ItemSettingTemplate(item = item, onIsTemplate = {isTemplate->
            item.isTemplate = isTemplate
            if( isTemplate) {
                item.setType(Type.TEMPLATE)
            }
            //item.type = if (it) Type.Template else "item"
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(4.dp))
        ItemSettingState(item = item, onChange = { done->
            item.setIsDone(done)
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(4.dp))
        ItemSettingCalendar(item = item, onEvent = {
            item.isCalenderItem = it
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(4.dp))
        ItemSettingAppointment(item = item, onEvent = {
            item.isAppointment = it
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(4.dp))
        ItemSettingPriority(priority = item.priority, onPriorityChanged= {
            item.priority = it
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(4.dp))
        TypeCard(type = item.getType(), onTypeChanged = {
            item.setType(it)
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(4.dp))
        ItemSettingCategory(
            item = item,
            categories = categories,
            onCategoryChanged = {
                item.category = it
                onEvent(ItemEvent.Update(item))
            },
            onAddNewCategory = { category ->
                item.category = category
                onEvent(ItemEvent.AddCategory(category))
            }

        )
        Spacer(modifier = Modifier.height(4.dp))
        DurationCard(duration = item.duration, onDurationChanged = {
            item.duration = it
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(4.dp))
        MentalCard(item = item, onMentalChanged = {
            item.mental = it
            onEvent(ItemEvent.Update(item))
        })
        Spacer(modifier = Modifier.height(4.dp))
        TimeLineCard(isTimeLineItem = item.getType().equals(Type.TIME_LINE)){
            item.setType(if( it) Type.TIME_LINE else Type.NODE)
            onEvent(ItemEvent.Update(item))
            onEvent(ItemEvent.Update(item))
        }
        Spacer(modifier = Modifier.height(4.dp))
        ParentCard(item = item)
        if( item.getType() == Type.MEDICIN) {
            val content = item.content as MedicineContent
            Text(text = "edit medicine")
            DosageCard(
                dosage = content.dosage,
                onDosageChanged = {
                    content.dosage = it
                    onEvent(ItemEvent.Update(item))
                }
            )
            DoctorCard(name = content.doctor) {
                content.doctor = it
                onEvent(ItemEvent.Update(item))
            }
            MedicineRemainingWithdrawalsCard(
                remainingWithdrawals = content.numTimes.toString(),
                onRemainingWithdrawalsChanged = {
                    println("onRemainingWithdrawalsChanged")
                    //content.numTimes = it.toInt()
                    //onEvent(ItemEvent
                }
            )
        }
    }
}



@Composable
@PreviewLightDark
fun PreviewItemEditorScreen(){
    val item = Item("trombyl")
    item.setType(Type.MEDICIN)
    item.content = MedicineContent().also {
        it.dosage = "1 tablett/morgon"
    }
    LucyTheme {
        ItemEditor(item = item,
            onEvent = {},
            onSave = {
            println("on event")
        })
        //ItemSettingsRow(item = Item("hello"), onEdit = {})
    }
}
