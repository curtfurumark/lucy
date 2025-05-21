package se.curtrune.lucy.screens.dev

import android.content.Context
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.util.Logger

object ItemsTest {
    private val repository = LucindaApplication.appModule.repository
    fun testDeleteTree(context: Context?) {
        Logger.log("...testDeleteTree()")
    }

    fun createTree(context: Context?) {
        Logger.log("...createFamilyItems()")
        var rootItem = Item("rootItem")
        val todoRoot = repository.getTodoRoot()
        rootItem = todoRoot?.let { repository.insertChild(it, rootItem) }!!
        var child1 = Item("child1")
        child1 = repository.insertChild(rootItem, child1)
        var child2  = Item("child2")
        child2 = repository.insertChild(rootItem, child2)
        var grandchild1 = Item("grandChild1")
        grandchild1 = repository.insertChild(child1, grandchild1)
        var grandChild2 = Item("grandChild2")
        grandChild2 = repository.insertChild(child1, grandChild2)
        println("rootItem id: ${rootItem.id}")
    }

    fun deleteTree(parent: Item, context: Context?) {
        Logger.log("...deleteTree(Item)", parent.heading)
        repository.deleteTree(parent)
    }

    fun deleteTree(parentID: Long, context: Context?) {
        Logger.log("ItemTest.deleteTree(long, Context)")
        val item = repository.selectItem(parentID)
        if (item == null) {
            Logger.log(" no item with that id found ")
            return
        }
        //ItemsWorker.VERBOSE = true
        repository.deleteTree(item)
    }
}
