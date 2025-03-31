package se.curtrune.lucy.screens.dev.test_cases

import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import java.time.LocalDate

class LocalDBTest {


    fun createTreeToDelete(){
        println("...createTreeToDelete()")
        var root = Item("root")
        root.targetDate = LocalDate.now()
        val db = LucindaApplication.appModule.sqliteLocalDB
        root = db.insert(root)
        val child1 = Item("child 1")
        val child2 = Item("child 2")
        db.insertChild(root, child1)
        db.insertChild(root, child2)
    }
}