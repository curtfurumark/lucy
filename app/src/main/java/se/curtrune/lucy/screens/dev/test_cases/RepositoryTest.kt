package se.curtrune.lucy.screens.dev.test_cases

import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item

class RepositoryTest {
    private val repository= LucindaApplication.repository

    fun testRestore(){
        println("RepositoryTest.testRestore()")
        val item = Item("delete and restore me, please")
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

    fun setChildrenGenerated(parentID: Long){
        println("setChildrenGenerated(parentID: $parentID)")
        //repository.selectChildren();


    }
}