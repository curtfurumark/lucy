package se.curtrune.lucy.modules

import android.app.Application
import android.content.Context
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.persist.ItemsWorker
import se.curtrune.lucy.persist.LocalDB
import se.curtrune.lucy.persist.Queeries
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.workers.NotificationsWorker
import java.time.LocalDate
import java.time.LocalDateTime

class Repository (val context: Application){
    init {
        println("init")
    }

    /**
     * spawns an item based on it's template Item
     * @param template, the template to use
     * @return a brand new spanking item instance, b
     */
    private fun createActualItem(template: Item): Item {
        println("createActual(Item: ${template.heading})")
        val item = Item()
        item.setType(item.type.ordinal)
        item.heading = template.heading
        item.comment = template.comment
        item.duration = template.duration
        item.category = template.category
        item.tags = template.tags
        item.targetTime = template.targetTime
        item.targetDate = LocalDate.now()
        item.setIsCalenderItem(template.isCalenderItem)
        //item.setEstimate(template.getEstimate());
        item.color = template.color
        item.state = State.DONE
        item.type = Type.TEMPLATE_CHILD
        item.anxiety = template.anxiety
        item.energy = template.energy
        item.mood = template.mood
        item.stress = template.stress
        return item
    }

    /**
     * deletes an item, but if it has children, //TODO delete children recursively
     * @param item, the item to delete from the db
     * @param context context context context
     * @return true if item was deleted false otherwise
     */
    fun delete(item: Item): Boolean {
        println("ItemsWorker.delete(Item) ${item.heading}")
        var res = false
        var rowsAffected: Int
        LocalDB(context).use { db ->
            rowsAffected = db.delete(item)
        }
        if (rowsAffected != 1) {
            println("ERROR deleting item ${item.heading}")
        } else {
            res = true
        }
        return res
    }

    /**
     * if item hasRepeat, creates instance up until RepeatWorker.maxDate
     * return the the template
     * if item has notification, sets notification
     * @param item, the item to be inserted
     * @param context context context
     * @return the inserted item, with its id field set to whatever db decided, autoincrement
     */
    fun insert(item: Item): Item? {

        println("Repository.insert(Item) ${item.heading}")
        if (item.hasNotification()) {
            NotificationsWorker.setNotification(item, context)
        }
        if (item.hasPeriod()) {
            return ItemsWorker.insertRepeat(item, context)
        }
        var itemWithID: Item? = null
        LocalDB(context).use { db ->
            itemWithID = db.insert(item)
        }
        return itemWithID
    }
    fun selectChildren(parent: Item?): List<Item> {
        println("Repository.selectChildren(Item)")
        LocalDB(context).use { db ->
            return db.selectItems(Queeries.selectChildren(parent))
        }
    }

    fun selectItems(date: LocalDate ): List<Item> {
        println("Peristory.selectItems(${date.toString()})")
        val queery = Queeries.selectItems(date)
        var items: List<Item>
        LocalDB(context).use { db ->
            items = db.selectItems(queery)
        }
        return items
    }

    /**
     * not in use right now, but i suspect it will be need some time in the not so distant future
     * @param type the type to be selected,
     * @param context context context context
     * @return a list of item of said type
     */
    fun selectItems(type: Type): List<Item> {
        val query = Queeries.selectItems(type)
        LocalDB(context).use { db ->
            return db.selectItems(query)
        }
    }

    fun update(item: Item): Int {
        Logger.log("ItemsWorker.update(Item, Context)", item.heading)
        if (item.isTemplate && item.isDone) {
            return updateTemplate(item)
        }
        LocalDB(context).use { db ->
            item.updated = LocalDateTime.now()
            return db.update(item)
        }
    }
    companion object{
        var VERBOSE = false
    }

    private fun updateTemplate(template: Item): Int {
        println("ItemsWorker.updateTemplate(Item: ${template.heading})")
        LocalDB(context).use { db ->
            if (template.isDone) {
                //if (ItemsWorker.VERBOSE) Logger.log("...template is done, will spawn a child")
                template.state = State.TODO
                var child = createActualItem(template)
                child.type = Type.TEMPLATE_CHILD
                child = db.insertChild(
                    template,
                    child
                ) //creates and inserts mental, or rather insert(Item) does
                if (template.hasPeriod()) {
                    template.updateTargetDate()
                } else {
                    template.targetDate = LocalDate.now()
                }
                template.duration = 0
            }
            return db.update(template)
        }
    }
}