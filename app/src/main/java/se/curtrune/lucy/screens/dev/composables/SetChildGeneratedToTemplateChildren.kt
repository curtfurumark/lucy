package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.persist.ItemsWorker

@Composable
fun SetGeneratedToTemplateChildren(){
    Card(){
        Column(modifier = Modifier.fillMaxWidth()) {
            var itemId by remember {
                mutableStateOf("0")
            }
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
            Text(text = "set children to TEMPLATE_CHILD", fontSize = 24.sp)
            Button(onClick = {
                println("on button clicked")
                setChildrenToGenerated(itemId.toLong())
            }) {
                Text(text = "set children")
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