package se.curtrune.lucy.modules

import android.app.Application
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.classes.calender.CalenderMonth
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.persist.CalenderWorker
import se.curtrune.lucy.persist.ItemsWorker
import se.curtrune.lucy.persist.SqliteLocalDB
import se.curtrune.lucy.persist.Queeries
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.workers.NotificationsWorker
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class Repository (val context: Application){
    private val mentalModule = LucindaApplication.mentalModule
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
        println("Repository.delete(${item.heading})")
        var res = false
        var rowsAffected: Int
        SqliteLocalDB(context).use { db ->
            rowsAffected = db.delete(item)
        }
        if (rowsAffected != 1) {
            println("ERROR deleting item ${item.heading}")
        } else {
            mentalModule.update()
            res = true
        }
        return res
    }
    fun delete(id: Long): Boolean {
        println("Repository.delete(id: $id)")
        var res = false
        var rowsAffected: Int
        SqliteLocalDB(context).use { db ->
            rowsAffected = db.delete(id)
        }
        if (rowsAffected != 1) {
            println("ERROR deleting item id: $id")
        } else {
            mentalModule.update()
            res = true
        }
        return res
    }

    fun getCalenderMonth(yearMonth: YearMonth): CalenderMonth {
        Logger.log("...getCalenderMonth(YearMonth)", yearMonth.toString())
        val calenderMonth = CalenderMonth(yearMonth)
        calenderMonth.calenderDates = CalenderWorker.getCalenderDates(
            calenderMonth.firstDate,
            calenderMonth.lastDate,
            context
        )
        return calenderMonth
    }
    fun getEvents(week: Week): List<CalenderDate> {
        Logger.log("CalendarWorker.getEvents(Week, Context)", week.toString())
        return CalenderWorker.getCalenderDates(week.firstDateOfWeek, week.lastDateOfWeek, context)
    }

    /**
     * @param item, the item for which you wish to get its parent
     * @param context context context context
     * @return the parent, TODO, null if not parent?
     */
    fun getParent(item: Item?): Item? {
        if( item == null){
            println("...getParent(Item) called with null item")
            return null
        }
        if (VERBOSE) println("Repository.getParent(${item.heading})")
        SqliteLocalDB(context).use { db ->
            return db.selectItem(item.parentId)
        }
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
        if (item.hasRepeat()) {
            return ItemsWorker.insertRepeat(item, context)
        }
        var itemWithID: Item? = null
        SqliteLocalDB(context).use { db ->
            itemWithID = db.insert(item)
        }
        mentalModule.update()
        return itemWithID
    }
    fun insertChild(parent: Item, child: Item): Item {
        if (VERBOSE) println("Repository.insertChild(Item, Item)")
        if (!parent.hasChild()) {
            Logger.log("....no children for this parent, yet, parent: ", parent.heading)
            ItemsWorker.setHasChild(parent, true, context)
        }
        child.parentId = parent.id
        if (child.hasRepeat()) {
            return ItemsWorker.insertRepeat(child, context)
        }
        SqliteLocalDB(context).use { db ->
            return db.insert(child)
        }
    }
    fun getAppointmentsRoot(): Item {
        val settings = Settings.getInstance(context)
        val id = settings.getRootID(Settings.Root.APPOINTMENTS)
        SqliteLocalDB(context).use { db ->
            return db.selectItem(id)
        }
    }
    fun restoreDeleted(item: Item): Item? {
        println("Repository.restoreDeleted(${item.heading})")
        var deletedItem: Item? = null
        SqliteLocalDB(context).use { db ->
            deletedItem = db.insert(item)
        }
        return deletedItem
    }
    fun search(filter: String): List<Item>{
        println("Repository.search($filter)")
        SqliteLocalDB(context).use { db ->
            return db.selectItems(Queeries.searchItems(filter))
        }

    }

    /**
     * selects all the items with type set to Appointment
     * @return all the items matching
     */
    fun selectAppointments(): List<Item> {
        println("Repository.selectAppointments()")
        val query = Queeries.selectAppointments()
        SqliteLocalDB(context).use { db ->
            return db.selectItems(query)
        }
    }
    fun selectChildren(parent: Item?): List<Item> {
        println("Repository.selectChildren(Item)")
        SqliteLocalDB(context).use { db ->
            return db.selectItems(Queeries.selectChildren(parent))
        }
    }
    fun selectItem(id: Long): Item? {
        println("Repository.selectItem(long, Context)")
        try {
            SqliteLocalDB(context).use { db ->
                return  db.selectItem(id)
            }
        } catch (e: Exception) {
            Logger.log("EXCEPTION", e.message)
        }
        return null
    }

    /**
     * selects all the items
     * @param context, context context
     * @return all the items in the table items
     */
    fun selectItems(): List<Item> {
        println("Repository.selectItems()")
        SqliteLocalDB(context).use { db ->
            return db.selectItems()
        }
    }

    fun selectItems(date: LocalDate ): List<Item> {
        println("Repository.selectItems(${date.toString()})")
        val queery = Queeries.selectItems(date)
        var items: List<Item>
        SqliteLocalDB(context).use { db ->
            items = db.selectItems(queery)
        }
        return items
    }
    fun selectItems(date: LocalDate, state: State): List<Item> {
        if(VERBOSE) println("Repository.selectItems(Date ${date.toString()}, State: ${state.toString()})")
        SqliteLocalDB(context).use { db ->
            return db.selectItems(Queeries.selectItems(date, state))
        }
    }

    /**
     * select done items as specified by argument
     * @param firstDate first date inclusive
     * @param lastDate last date inclusive
     * @param context just the frigging context
     * @return a list as specified
     */
    fun selectItems(firstDate: LocalDate?, lastDate: LocalDate?): List<Item> {
        println("Repository.selectItems($firstDate, $lastDate)")
        SqliteLocalDB(context).use { db ->
            val query = Queeries.selectItems(firstDate, lastDate, State.DONE)
            return db.selectItems(query)
        }
    }
    fun selectItems(state: State): List<Item> {
        println("Repository.selectItems(state: ${state.toString()})")
        SqliteLocalDB(context).use { db ->
            return db.selectItems(Queeries.selectItems(state))
        }
    }

    /**
     * not in use right now, but i suspect it will be need some time in the not so distant future
     * @param type the type to be selected,
     * @param context context context context
     * @return a list of item of said type
     */
    fun selectItems(type: Type): List<Item> {
        val query = Queeries.selectItems(type)
        SqliteLocalDB(context).use { db ->
            return db.selectItems(query)
        }
    }
    fun selectTemplateChildren(parent: Item?): List<Item> {
        SqliteLocalDB(context).use { db ->
            return db.selectItems(Queeries.selectTemplateChildren(parent))
        }
    }

    fun update(item: Item): Int {
        println("Repository.update(${item.heading})")
        if (item.isTemplate && item.isDone) {
            mentalModule.update()
            return updateTemplate(item)
        }
        SqliteLocalDB(context).use { db ->
            item.updated = LocalDateTime.now()
            return db.update(item)
        }
    }
    companion object{
        var VERBOSE = false
    }

    private fun updateTemplate(template: Item): Int {
        println("Repository.updateTemplate(Item: ${template.heading})")
        SqliteLocalDB(context).use { db ->
            if (template.isDone) {
                //if (ItemsWorker.VERBOSE) Logger.log("...template is done, will spawn a child")
                template.state = State.TODO
                var child = createActualItem(template)
                child.type = Type.TEMPLATE_CHILD
                child = db.insertChild(
                    template,
                    child
                ) //creates and inserts mental, or rather insert(Item) does
                if (template.hasRepeat()) {
                    template.updateTargetDate()
                } else {
                    template.targetDate = LocalDate.now()
                }
                template.duration = 0
            }
            return db.update(template)
        }
    }
    fun touchParents(item: Item) {
        println("Repository.touchParents()")
        SqliteLocalDB(context).use { db ->
            db.touchParents(item)
        }
    }

    fun getTodoRoot(): Item {
        val settings = Settings.getInstance(context)
        val id = settings.getRootID(Settings.Root.TODO)
        SqliteLocalDB(context).use { db ->
            return db.selectItem(id)
        }
    }
}