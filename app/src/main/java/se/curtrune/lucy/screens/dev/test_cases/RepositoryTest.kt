package se.curtrune.lucy.screens.dev.test_cases

import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.persist.Queeries

class RepositoryTest {
    private val repository= LucindaApplication.appModule.repository

    fun testRestore(){
        println("RepositoryTest.testRestore()")
        val item =
            Item("delete and restore me, please")
        var itemWithId = repository.insert(item)
        if (itemWithId != null) {
            println("item inserted with id ${itemWithId.id})")
        }else{
            println("error inserting item")
            return
        }
        val deleted = repository.delete(itemWithId)
        if( !deleted){
            println("...error deleting item")
            return
        }
        repository.restoreDeleted(itemWithId)


    }
    fun testGetWeekCalendar(){
        println("testGetWeekCalendar")
        val items = repository.selectItems(Queeries.selectAllWeekItems(Week()))
        items.forEach{ item ->
            println("${item.heading} ${item.itemDuration?.type?.name}")
        }

    }

    fun setChildrenGenerated(parentID: Long){
        println("setChildrenGenerated(parentID: $parentID)")
        //repository.selectChildren();


    }
}