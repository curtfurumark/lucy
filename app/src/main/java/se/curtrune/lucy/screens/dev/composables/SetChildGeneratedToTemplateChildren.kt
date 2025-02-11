package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.persist.ItemsWorker
import se.curtrune.lucy.screens.dev.DevEvent
import se.curtrune.lucy.screens.dev.DevState
import se.curtrune.lucy.screens.item_editor.ItemEvent
import se.curtrune.lucy.screens.item_editor.composables.EditHasChild

@Composable
fun SetGeneratedToTemplateChildren(state: DevState, onEvent: (ItemEvent)->Unit){
    Card(){
        //var state = MutableStateFlow(DevState())
        val item by remember {
            mutableStateOf(state.currentItem)
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            var itemId by remember {
                mutableStateOf("0")
            }
            var itemHeading by remember {
                mutableStateOf("")
            }
            Text(text = "set children to TEMPLATE_CHILD", fontSize = 24.sp)
            OutlinedTextField(
                value = itemId,
                onValueChange = { itemId = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = {
                    Text(text = "item id")
                }
            )
            Text(text = if( state.currentItem != null) state.currentItem!!.heading else "")
            state.currentItem.let {
                if (it != null) {
                    EditHasChild(item = it, onEvent = onEvent)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    onEvent(ItemEvent.GetItem(itemId.toLong()))
                }) {
                    Text(text = "get item")
                }
                Button(onClick = {
                    println("on button clicked")
                    if(state.currentItem != null){
                        onEvent(ItemEvent.GetChildren(state.currentItem!!))
                    }
                }) {
                    Text(text = "show children")
                }

            }
            Row(modifier = Modifier.fillMaxWidth()){
                Button(onClick = {
                    println("set all children to template children")
                    state.items.forEach{ item->
                        item.type = Type.TEMPLATE_CHILD
                        onEvent(ItemEvent.Update(item))
                    }
                }){
                    Text(text = "set all")
                }
                Button(onClick = {
                    println("show template children")
                    if( state.currentItem != null){
                        onEvent(ItemEvent.GetChildrenType(state.currentItem!!, Type.TEMPLATE_CHILD))
                    }
                }){
                    Text(text = "show template children")
                }
            }
            Text(text = "number of items: ${state.items.size.toString()}", fontSize = 20.sp)
            state.items.forEach{ item->
                println("forEach ${item.heading}")
                ItemWithType(item = item, onEvent = {
                    //println("on event")
                    onEvent(ItemEvent.Update(item))
                })
                Spacer(modifier = Modifier.height(4.dp))
            }

        }
    }
}

/**
 * a hack to cover the tracks of previous mistakes
 */
fun setChildrenToGenerated(parentID: Long){
    println("setChildrenToGenerated($parentID)")
    val localDB = LucindaApplication.localDB
    val parent = localDB.selectItem(parentID)
    if( parent == null){
        println("could not find an item with aforementioned id")
        return
    }
    val children = localDB.getChildren(parent)
    children.forEach{ item->
        if( item.type != Type.TEMPLATE_CHILD){
            item.type = Type.TEMPLATE_CHILD
            localDB.update(item)
        }
    }
    println("setting has child to false for parent")
    parent.setHasChild(false)
    localDB.update(parent)
    println("...done, i hope")
}


fun getItem(id: Long): Item? {
    val localDB = LucindaApplication.localDB
    return localDB.selectItem(id)
}


