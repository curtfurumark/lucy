package se.curtrune.lucy.persist

import android.app.Application
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.calender.CalendarWeek
import se.curtrune.lucy.classes.calender.CalenderMonth
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.item.Repeat
import se.curtrune.lucy.features.google_calendar.GoogleCalendarModule
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.features.notifications.NotificationsWorker
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class Repository (val context: Application){
    //private val mentalModule = LucindaApplication.mentalModule
    init {
        println("Repository.init{}")
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
     * @return true if item was deleted false otherwise
     */
    fun delete(item: Item): Boolean {
        println("Repository.delete(${item.heading})")
        var res = false
        val db = SqliteLocalDB(context)
        val rowsAffected = db.delete(item)
        if (rowsAffected != 1) {
            println("ERROR deleting item ${item.heading}")
        } else {
            //mentalModule.update()
            res = true
        }
        return res
    }
    fun delete(id: Long): Boolean {
        println("Repository.delete(id: $id)")
        var res = false
        val db = SqliteLocalDB(context)
        val rowsAffected = db.delete(id)
        if (rowsAffected != 1) {
            println("ERROR deleting item id: $id")
        } else {
            //mentalModule.update()
            res = true
        }
        return res
    }

    fun getCalenderMonth(yearMonth: YearMonth): CalenderMonth {
        println("Repository.getCalenderMonth($yearMonth)")
        val calenderMonth = CalenderMonth(yearMonth)
        calenderMonth.calenderDates = CalendarHelper.getCalenderDates(
            calenderMonth.firstDate,
            calenderMonth.lastDate,
            context
        )
        return calenderMonth
    }
    fun getCalendarWeek(week: Week): CalendarWeek {
        println("Repository.getCalendarWeek($week)")
        return CalendarHelper.getCalendarWeek(week)
    }

    /**
     * @param item, the item for which you wish to get its parent
     * @return the parent, TODO, null if not parent?
     */
    fun getParent(item: Item?): Item? {
        if( item == null){
            println("...getParent(Item) called with null item")
            return null
        }
        if (VERBOSE) println("Repository.getParent(${item.heading})")
        val db = SqliteLocalDB(context)
        val parent = db.getParent(item)
        db.close()
        return parent
    }
    fun getRootItem(root: Settings.Root): Item? {
        println("Repository.getRootItem(Settings.Root ${root.name})")
        val settings = Settings.getInstance(context)
        var rootID: Long = -1
        when (root) {
            Settings.Root.APPOINTMENTS -> rootID = settings.getRootID(Settings.Root.APPOINTMENTS)
            Settings.Root.TODO -> rootID = settings.getRootID(Settings.Root.TODO)
            Settings.Root.DAILY -> rootID = settings.getRootID(Settings.Root.DAILY)
            Settings.Root.PROJECTS -> rootID = settings.getRootID(Settings.Root.PROJECTS)
            Settings.Root.PANIC -> rootID = settings.getRootID(Settings.Root.PANIC)
            Settings.Root.THE_ROOT -> rootID = settings.getRootID(Settings.Root.THE_ROOT)
        }
        println("root id:$root ")
        val db = SqliteLocalDB(context)
        val item = db.selectItem(rootID)
        db.close()
        return item
    }

    /**
     * return the the template
     * if item has notification, sets notification
     * @param item, the item to be inserted
     * @return the inserted item, with its id field set to whatever db decided, autoincrement
     */
    fun insert(item: Item): Item? {
        println("Repository.insert(Item) ${item}")
        if (item.hasNotification()) {
            NotificationsWorker.setNotification(item, context)
        }
        if (item.isSyncWithGoogle) {
            val userSettings = LucindaApplication.appModule.userSettings
            val calendarID = userSettings.googleCalendarId
            GoogleCalendarModule(context).insertItem(item, calendarID)
        }
        if (item.hasRepeat()) {
            return insertRepeat(item)
        }
        val db = SqliteLocalDB(context)
        val itemWithID = db.insert(item)
        db.close()
        //mentalModule.update()
        return itemWithID
    }
    fun insert(repeat: Repeat): Repeat? {
        println("Repository.insert(Repeat)")
        try {
            SqliteLocalDB(context).use { db ->
                return  db.insert(repeat)
            }
        } catch (e: java.lang.Exception) {
            Logger.log("EXCEPTION", e.message)
            return null
        }
    }
    fun insertChild(parent: Item, child: Item): Item {
        if (VERBOSE) println("Repository.insertChild(Item, Item)")
        if (!parent.hasChild()) {
            println("....no children for this parent, yet, parent: ${parent.heading}")
            setHasChild(parent, true)
        }
        child.parentId = parent.id
        if (child.hasRepeat()) {
            return createInstances(child)
        }
        val db = SqliteLocalDB(context)
        val itemWithID = db.insert(child)
        db.close()
        return itemWithID
    }

    private fun createInstances(template: Item):  Item{
        println("createInstances($template)")
        val items = Repeater.createInstances(template)
        insert(items)
        return template
    }
    fun getAppointmentsRoot(): Item? {
        val settings = Settings.getInstance(context)
        val id = settings.getRootID(Settings.Root.APPOINTMENTS)
        val db = SqliteLocalDB(context)
        val item = db.selectItem(id)
        db.close()
        return item
    }
    fun insert(items: List<Item>){
        val db = SqliteLocalDB(context)
        db.insert(items)
        db.close()
    }
    private fun insertRepeat(template: Item): Item?{
        println("insertRepeat()")
        if( !template.hasRepeat()){
            println("should not happen, insertRepeat should only be called if item has repeat")
            return null
        }
/*        SqliteLocalDB(context).use{db->
            template.type = Type.REPEAT_TEMPLATE
            val templateWithID = db.insert(template)
            val repeat = template.repeat
            repeat.templateID = templateWithID.id
            val repeatWithId = db.insert(template.repeat)
            templateWithID.repeatID = repeatWithId!!.id
            db.update(templateWithID)
            val items = Repeater.createInstances(templateWithID)
            db.insert(items)
            return templateWithID
        }*/
        val db = SqliteLocalDB(context)
        template.type = Type.REPEAT_TEMPLATE
        val templateWithID = db.insert(template)
        val repeat = template.repeat
        repeat.templateID = templateWithID.id
        val repeatWithID = db.insert(template.repeat)
        templateWithID.repeatID = repeatWithID!!.id
        db.update(templateWithID)
        val items = Repeater.createInstances(templateWithID)
        db.insert(items)
        return templateWithID
    }
    fun restoreDeleted(item: Item): Item {
        println("Repository.restoreDeleted(${item.heading})")
        //var deletedItem: Item? = null
        val db = SqliteLocalDB(context)
        val insertedItem = db.insert(item)
        db.close()
        return insertedItem
    }
    fun search(filter: String): List<Item>{
        println("Repository.search($filter)")
        val db = SqliteLocalDB(context)
        val items = db.selectItems(Queeries.searchItems(filter))
        db.close()
        return items
    }
    /**
     * selects all the items with type set to Appointment
     * @return all the items matching
     */
    fun selectAppointments(): List<Item> {
        println("Repository.selectAppointments()")
        val query = Queeries.selectAppointments()
        val db = SqliteLocalDB(context)
        val items = db.selectItems(query)
        db.close()
        return items
    }
    fun selectChildren(parent: Item?): List<Item> {
        println("Repository.selectChildren(Item: ${parent?.heading})")
        val db =   SqliteLocalDB(context)
        val items =  db.selectItems(Queeries.selectChildren(parent))
        db.close()
        return items
    }
    fun selectItem(id: Long): Item? {
        println("Repository.selectItem(long, Context)")
        val db = SqliteLocalDB(context)
        val item = db.selectItem(id)
        db.close()
        return item
    }

    /**
     * selects all the items
     * @return all the items in the table items
     */
    fun selectItems(): List<Item> {
        println("Repository.selectItems()")
        val db= SqliteLocalDB(context)
        val items = db.selectItems()
        db.close()
        return items
    }

    fun selectItems(date: LocalDate ): List<Item> {
        println("Repository.selectItems($date)")
        val queery = Queeries.selectItems(date)
        val db = SqliteLocalDB(context)
        val items = db.selectItems(queery)
        return items
    }
    fun selectItems(date: LocalDate, state: State): List<Item> {
        if(VERBOSE) println("Repository.selectItems(Date $date, State: $state)")
        val db = SqliteLocalDB(context)
        val items = db.selectItems(Queeries.selectItems(date, state))
        db.close()
        return items
    }
    fun selectItems(query: String):List<Item>{
        println("selectItems($query)")
        val db = SqliteLocalDB(context)
        val items = db.selectItems(query)
        db.close()
        return items
    }

    /**
     * selects all the items for week where field isCalendarItem is set to 1
     */
    fun selectItems(week: Week, calendarItem: Boolean): List<Item>{
        println("Repository.selectItems($week)")
        val db = SqliteLocalDB(context)
        val items = db.selectItems(Queeries.selectItems(week, calendarItem) )
        db.close()
        return items
    }

    /**
     * select done items as specified by argument
     * @param firstDate first date inclusive
     * @param lastDate last date inclusive
     * @return a list as specified
     */
    fun selectItems(firstDate: LocalDate?, lastDate: LocalDate?, state: State): List<Item> {
        println("Repository.selectItems($firstDate, $lastDate, state: ${state.name})")
        val db = SqliteLocalDB(context)
        val items = db.selectItems(Queeries.selectItems(firstDate, lastDate, state))
        db.close()
        return items
    }
    fun selectItems(firstDate: LocalDate?, lastDate: LocalDate?): List<Item> {
        println("Repository.selectItems($firstDate, $lastDate)")
        val db = SqliteLocalDB(context)
        val items = db.selectItems(Queeries.selectItems(firstDate, lastDate))
        db.close()
        return items
    }
    fun selectItems(state: State): List<Item> {
        println("Repository.selectItems(state: $state)")
        val db = SqliteLocalDB(context)
        val items = db.selectItems(Queeries.selectItems(state))
        db.close()
        return items
    }

    /**
     * @param type the type to be selected,
     * @return a list of item of said type
     */
    fun selectItems(type: Type): List<Item> {
        println("Repository.selectItems(type: $type)")
        val query = Queeries.selectItems(type)
        println(query)
        val db = SqliteLocalDB(context)
        val items = db.selectItems(query)
        db.close()
        return items
    }
    fun selectRepeat(id: Long): Repeat? {
        println("ItemsWorker.selectRepeat(id: $id)")
        SqliteLocalDB(context).use { db ->
            return db.selectRepeat(id)
        }
    }
    fun deleteTree(parent: Item) {
        Logger.log("ItemsWorker.deleteTree(Item, Context)", parent.heading)
        if (parent.hasChild()) {
            val children = selectChildren(parent)
            for (item in children) {
                deleteTree(item)
            }
        }
        Logger.log("DELETE ME", parent.heading)
        val stat = delete(parent)
        if (!stat) {
            Logger.log("ERROR DELETING ITEM")
        }
    }
    fun selectRepeats(): List<Repeat>{
        val db = SqliteLocalDB(context)
        val repeats = db.selectRepeats()
        db.close()
        return repeats
    }
    fun selectTemplateChildren(parent: Item?): List<Item> {
        val db = SqliteLocalDB(context)
        val children = db.selectItems(Queeries.selectTemplateChildren(parent))
        db.close()
        return children
    }
    fun selectTree(parentID: Long): Item?{
        val db = SqliteLocalDB(context)
        val item = db.selectItem(parentID)
        if(  item != null && item.hasChild()){
            val children = selectChildren(item)
            item.children = children
        }
        db.close()
        return item
    }
    private fun selectTree(): List<Item> {
        val db = SqliteLocalDB(context)
        //val items = db.selectItems()
        db.close()
        return emptyList()
    }

    private fun setHasChild(item: Item, hasChild: Boolean) {
        println("Repository.setHasChild(Item: $item.heading, hasChild: $hasChild)")
        val db = SqliteLocalDB(context)
        db.setItemHasChild(item.id, hasChild)
        db.close()
    }

    fun update(item: Item): Int {
        println("Repository.update(${item.heading})")
        if (item.isTemplate && item.isDone) {
            //mentalModule.update()
            return updateTemplate(item)
        }
        val db = SqliteLocalDB(context)
        item.updated = LocalDateTime.now()
        val rowsAffected = db.update(item)
        db.close()
        return rowsAffected
    }
    fun update(repeat: Repeat): Int {
        println("Repository.update(Repeat)")
        val db = SqliteLocalDB(context)
        val rowsAffected = db.update(repeat)
        db.close()
        return rowsAffected
    }
    companion object{
        var VERBOSE = false
    }

    private fun updateTemplate(template: Item): Int {
        println("Repository.updateTemplate(Item: ${template.heading})")
        val db = SqliteLocalDB(context)
        if (template.isDone) {
            template.state = State.TODO
            var child = createActualItem(template)
            child.type = Type.TEMPLATE_CHILD
            child = db.insertChild(
                template,
                child
            )
            if (template.hasRepeat()) {
                template.updateTargetDate()
            } else {
                template.targetDate = LocalDate.now()
            }
            template.duration = 0
        }
        val rowsAffected = db.update(template)
        db.close()
        return rowsAffected
    }
    fun touchParents(item: Item) {
        println("Repository.touchParents()")
        val db = SqliteLocalDB(context)
        db.touchParents(item)
        db.close()
    }

    fun getTodoRoot(): Item? {
        val settings = Settings.getInstance(context)
        val id = settings.getRootID(Settings.Root.TODO)
        val db = SqliteLocalDB(context)
        val item = db.selectItem(id)
        db.close()
        return item
    }

    fun selectItems(week: Week): List<Item> {
        return selectItems(week.firstDateOfWeek, week.lastDateOfWeek)
    }
}