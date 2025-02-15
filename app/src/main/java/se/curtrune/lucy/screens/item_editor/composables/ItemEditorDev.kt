package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.util.Converter


@Composable
fun ItemEditorDev(item: Item, onEvent: (ItemEvent)->Unit) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "advanced", fontSize = 24.sp, color = Color.White)
        Text(text = "id: ${item.id}", color = Color.White)
        EditViewParent(item, onEvent = onEvent)
        Spacer(modifier = Modifier.height(4.dp))
        EditHasChild(item, onEvent = onEvent)
        Spacer(modifier = Modifier.height(4.dp))
        EditType(item, onEvent = onEvent)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "created: ${Converter.format(item.created)}", color = Color.White)
        Text(text = "updated: ${Converter.format(item.updated)}", color = Color.White)
    }
}




@Composable
fun EditViewParent(item: Item, onEvent: (ItemEvent) -> Unit){
    val repository = LucindaApplication.repository
    val parent = repository.getParent(item)
    var todoList by remember {
        mutableStateOf(false)
    }
    var newParentID by remember {
        mutableStateOf("not implemented")
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column() {
            Text(text = "parent info", fontSize = 20.sp)
            Text(text = "parent: ${item.parentId}", color = Color.White)
            Text(text = "heading: ${parent?.heading}", color = Color.White)
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = todoList, onCheckedChange = {
                    todoList = !todoList
                })
                Text(text = "todo list")
            }
            OutlinedTextField(
                value = newParentID,
                onValueChange = {
                    newParentID = it
                },
                label = {Text(text = "parent id")}
            )
            Button( onClick = {
                if(todoList) {
                    item.parentId = LucindaApplication.settings.getRootID(Settings.Root.TODO)
                }else{
                    item.parentId = newParentID.toLong()
                }
                onEvent(ItemEvent.Update(item))
            }){
                Text(text = "set parent")
            }
        }
    }
}

@Composable
fun EditHasChild(item: Item, onEvent: (ItemEvent) -> Unit){
    var hasChild by remember{
        mutableStateOf(item.hasChild())
    }
    Card(modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){
            Checkbox(checked = hasChild, onCheckedChange = {
                hasChild = it
                item.setHasChild(hasChild)
                onEvent(ItemEvent.Update(item))
            })
            Text(text = "hasChild", color = Color.White)
        }
    }
}
@Composable
fun EditParent(item: Item, onEvent: (ItemEvent) -> Unit){
    val repository = LucindaApplication.repository
    val parent = repository.getParent(item)
    val showChoseParentDialog by remember {
        mutableStateOf(false)
    }
    Card(modifier =  Modifier.fillMaxWidth()){
        Text(text = "parent")
        if (parent != null) {
            Text(text = "parent id: ${parent.id}")
        }
        if (parent != null) {
            Text(text = "parent heading: ${parent.heading}")
        }
    }
}
@Composable
fun EditType(item: Item, onEvent: (ItemEvent)->Unit){
    var dropdownExpanded by remember {
        mutableStateOf(false)
    }
    var currentType by remember {
        mutableStateOf(item.type)
    }
    Card(modifier = Modifier.fillMaxWidth()){
        Column() {
            Text(text = "choose new type")
            Text(text = currentType.name,
                modifier = Modifier.clickable {
                    dropdownExpanded = !dropdownExpanded
                })
            DropdownMenu(expanded = dropdownExpanded, onDismissRequest = {
                dropdownExpanded = false
            }) {
                Type.entries.forEach {type->
                    DropdownMenuItem(
                        text ={Text(text =  type.name)}
                        , onClick = {
                           println("type chosen: $type")
                            currentType = type
                            item.type = type
                            onEvent(ItemEvent.Update(item))
                            dropdownExpanded = false
                    })
                }

            }
        }
    }
}
@Composable
fun ChoseParentDialog(onDismiss: ()->Unit){
    Dialog(onDismissRequest = onDismiss) {


    }

}

@Composable
@Preview
fun PreviewItemEditor(){
    ItemEditorDev(item = Item("hello"), onEvent = {})
}