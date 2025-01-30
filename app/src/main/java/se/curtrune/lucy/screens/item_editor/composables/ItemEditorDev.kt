package se.curtrune.lucy.screens.item_editor.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.screens.util.Converter


@Composable
fun ItemEditorDev(item: Item){

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "advanced", fontSize = 24.sp, color = Color.White)
        Text(text = "id: ${item.id}", color = Color.White)
        EditViewParent(item)
        Text(text = "has child/ren ${item.hasChild()}",color =Color.White)
        Text(text = "created: ${Converter.format(item.created)}",color =Color.White)
        Text(text = "updated: ${Converter.format(item.updated)}",color =Color.White)
        Text(text = "type: ${item.type.toString()}",color =Color.White)
    }
}
@Composable
fun EditViewParent(item: Item){
    val repository = LucindaApplication.repository
    val parent = repository.getParent(item)
    println("got parent ${parent?.heading}")
    Text(text = "parent: ${item.parentId}", color =Color.White)
    Text(text = "heading: ${parent?.heading}", color = Color.White)
}

@Composable
fun EditHasChild(){

}