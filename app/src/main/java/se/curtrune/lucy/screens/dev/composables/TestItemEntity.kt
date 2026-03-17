package se.curtrune.lucy.screens.dev.composables

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.room.Room
import kotlinx.coroutines.launch
import se.curtrune.lucy.persist.ItemDatabase
import se.curtrune.lucy.persist.ItemEntity

@Composable
fun TestItemEntity(context: Context){
    val dao = Room.databaseBuilder(context, ItemDatabase::class.java, "item_database").build().itemDao()
    val item = ItemEntity(heading = "test")
    val scope = rememberCoroutineScope()
    scope.launch {
        dao.insert(item)
    }
    //dao.insert(item)


}