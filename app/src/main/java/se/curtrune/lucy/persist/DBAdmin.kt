package se.curtrune.lucy.persist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Environment
import com.google.gson.Gson
import se.curtrune.lucy.activities.economy.classes.Asset
import se.curtrune.lucy.activities.economy.classes.Transaction
import se.curtrune.lucy.activities.economy.persist.ECDBAdmin
import se.curtrune.lucy.activities.economy.persist.EcQueeries
import se.curtrune.lucy.app.App
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.classes.ItemDuration
import se.curtrune.lucy.classes.MedicineContent
import se.curtrune.lucy.features.notifications.Notification
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.item.Repeat
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.util.gson.MyGson.getMyGson
import java.io.File
import java.util.function.Consumer

class DBAdmin {
    val dataBaseFile: File?
        get() {
            Logger.log("...getDataBaseFile()")

            return null
        }

    companion object {
        var VERBOSE: Boolean = false
        var settings: Settings? = null

        @Deprecated("")
        fun addColumnRepeatIdToTableItems(context: Context?) {
            Logger.log("DBAdmin.alterColumnRepeatIdToTableItems(Context)")
            val queery = Queeries.ADD_COLUMN_REPEAT_ID_TO_ITEMS
            SqliteLocalDB(context).use { db ->
                db.executeSQL(queery)
            }
        }
        fun backupDataBase(context: Context){
            var externalDir = Environment.getExternalStorageDirectory()
            println("external dir: ${externalDir.absolutePath}")
            println("DBAdmin. backupDatabase(Context)")
            val dbName = SqliteLocalDB.dbName
            val sourceFile = context.getDatabasePath(dbName)
            val destinationFile = context.getExternalFilesDir(dbName)
            println("absolute path of database. ${sourceFile.absolutePath}")
            println("destination: ${destinationFile!!.absolutePath}")
/*            FileInputStream(sourceFile).use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }
            if (destinationFile.exists() && destinationFile.length() == sourceFile.length()) {
                println("Database backup successful: ${destinationFile.absolutePath}")
            } else {
                println("Database backup failed.")
            }*/
        }

        @JvmStatic
        fun createTables(context: Context?) {
            println("DBAdmin.createTables()")
            val db = SqliteLocalDB(context)
            //SqliteLocalDB(context).use { db ->
            db.executeSQL(Queeries.DROP_TABLE_ITEMS)
            db.executeSQL(Queeries.CREATE_TABLE_ITEMS)
            db.executeSQL(EcQueeries.DROP_TABLE_TRANSACTIONS)
            db.executeSQL(EcQueeries.CREATE_TABLE_TRANSACTIONS)
            db.executeSQL(EcQueeries.DROP_TABLE_ASSETS)
            db.executeSQL(EcQueeries.CREATE_TABLE_ASSETS)
            db.executeSQL(Queeries.DROP_TABLE_REPEAT)
            db.executeSQL(Queeries.CREATE_TABLE_REPEAT)
            Logger.log("...tables created")
            db.close()
            //}
        }

        fun createEconomyTables(context: Context?) {
            Logger.log(".DBAdmin.createEconomyTables(Context)")
            ECDBAdmin.createEconomyTables(context)
            listTables(context)
        }

        fun createItemsTable(context: Context?) {
            Logger.log("DBAdmin.createItemsTable()")
            SqliteLocalDB(context).use { db ->
                db.executeSQL(Queeries.CREATE_TABLE_ITEMS)
            }
        }

        fun createRepeatTable(context: Context?) {
            Logger.log("...createRepeatTable")
            SqliteLocalDB(context).use { db ->
                db.executeSQL(Queeries.CREATE_TABLE_REPEAT)
            }
        }

        fun dropTableItems(context: Context?) {
            SqliteLocalDB(context).use { db ->
                db.executeSQL(Queeries.DROP_TABLE_ITEMS)
            }
        }

        fun dropTableMental(context: Context?) {
            Logger.log("DBAdmin.dropTableMental(Context)")
            SqliteLocalDB(context).use { db ->
                db.executeSQL(Queeries.DROP_TABLE_MENTAL)
            }
        }

        fun dropTableRepeat(context: Context?) {
            Logger.log("...dropTableRepeat(Context)")
            SqliteLocalDB(context).use { db ->
                db.executeSQL(Queeries.DROP_TABLE_REPEAT)
            }
        }

        @JvmStatic
        fun dropTables(context: Context?) {
            Logger.log("DBAdmin.dropTables()")
            SqliteLocalDB(context).use { db ->
                db.executeSQL(Queeries.DROP_TABLE_ITEMS)
                db.executeSQL(Queeries.DROP_TABLE_MENTAL)
                db.executeSQL(Queeries.DROP_TABLE_REPEAT)
                db.executeSQL(EcQueeries.DROP_TABLE_ASSETS)
                db.executeSQL(EcQueeries.DROP_TABLE_TRANSACTIONS)
            }
            Logger.log("...tables dropped")
        }

        fun listColumns(table: String, context: Context?) {
            Logger.log("RepeatTest.listColumns(String table)")
            SqliteLocalDB(context).use { db ->
                db.getColumns(table)
            }
        }

        fun getItem(cursor: Cursor): Item {
            val item = Item()
            //Gson gson = new Gson();
            val gson = getMyGson()
            item.id= cursor.getLong(0)
            item.heading = cursor.getString(1)
            item.comment = cursor.getString(2)
            item.tags = cursor.getString(3)
            item.setCreated(cursor.getLong(4))
            item.setUpdated(cursor.getLong(5))
            item.setTargetDate(cursor.getLong(6))
            //item.setTargetTime(cursor.getInt(7))
            item.targetTimeSecondOfDay = cursor.getInt(7)
            item.category = cursor.getString(8)
            item.type = cursor.getInt(9)
            item.state = cursor.getInt(10)
            item.setHasChild(cursor.getInt(11) == 1)
            item.duration = cursor.getLong(12)
            item.parentId = cursor.getInt(13).toLong()
            item.isCalenderItem = cursor.getInt(14) == 1
            /*        String jsonRepeat = cursor.getString(15);
        if( jsonRepeat != null){
            Repeat repeat = gson.fromJson(jsonRepeat, Repeat.class);
            item.setRepeat(repeat);
        }*/
            if (cursor.getString(16) != null) {
                val json = cursor.getString(16)
                Logger.log("item duration json  ", json)
                try {
                    val itemDuration = gson.fromJson(json, ItemDuration::class.java)
                    item.itemDuration = itemDuration
                } catch (e: Exception) {
                    Logger.log("exception", e.message)
                    e.printStackTrace()
                }

                //item.setDurationType();
            }
            val notification = gson.fromJson(
                cursor.getString(17),
                Notification::class.java
            )
            item.notification = notification
            item.isTemplate = cursor.getInt(18) != 0
            val jsonContent = cursor.getString(19)
            if (jsonContent != null && !jsonContent.isEmpty()) {
                val ordinal = cursor.getInt(9)
                if (ordinal == Type.MEDICIN.ordinal) {
                    val content = gson.fromJson(
                        jsonContent,
                        MedicineContent::class.java
                    )
                    item.content = content
                }
            }
            //item.setReward(cursor.getString(20));
            item.color = cursor.getInt(21)
            item.priority = cursor.getInt(22)
            item.energy = cursor.getInt(23)
            item.anxiety = cursor.getInt(24)
            item.stress = cursor.getInt(25)
            item.mood = cursor.getInt(26)
            item.repeatID = cursor.getInt(27).toLong()
            return item
        }

        fun getContentValues(asset: Asset): ContentValues {
            val cv = ContentValues()
            cv.put("account", asset.account)
            cv.put("amount", asset.amount)
            cv.put("date", asset.date.toEpochDay())
            return cv
        }

        fun getContentValues(item: Item): ContentValues {
            if (VERBOSE) Logger.log("DBAdmin.getContentValues(Item)")
            val gson = getMyGson()
            val cv = ContentValues()
            cv.put("heading", item.heading)
            cv.put("comment", item.comment)
            cv.put("tags", item.tags)
            cv.put("created", item.createdEpoch)
            cv.put("updated", item.updatedEpoch)
            cv.put("targetDate", item.targetDateEpochDay)
            cv.put("targetTime", item.targetTimeSecondOfDay)
            cv.put("type", item.getType().ordinal)
            cv.put("state", item.getState().ordinal)
            cv.put("hasChild", if (item.hasChild()) 1 else 0)
            cv.put("duration", item.duration)
            cv.put("isCalenderItem", if (item.isCalenderItem) 1 else 0)
            cv.put("category", item.category)
            cv.put("parentID", item.parentId)
            if (item.repeat != null) {
                //cv.put("repeat", item.getRepeat().toJson());
                cv.put("repeat", gson.toJson(item.repeat))
            }
            //well change name of field, please TODO
            if (item.itemDuration != null) {
                Logger.log("item has duration, adding to field estimate")
                cv.put("estimate", gson.toJson(item.itemDuration))
            }
            /*        if( item.getEstimate() != null){
            cv.put("estimate", item.getEstimate().toJson());
        }*/
            if (item.hasNotification()) {
                cv.put("notification", gson.toJson(item.notification))
            }
            cv.put("template", if (item.isTemplate) 1 else 0)
            cv.put("color", item.color)
            cv.put("priority", item.priority)
            cv.put("energy", item.energy)
            cv.put("anxiety", item.anxiety)
            cv.put("stress", item.stress)
            cv.put("mood", item.mood)
            if (item.content != null) {
                cv.put("content", gson.toJson(item.content))
            }
            /*        if( item.getType().equals(Type.MEDIA)){
            cv.put("content",gson.toJson(item.getContent()) );
        }*/
            if (item.id > 0) { // for restoring deleted item,to ensure they don't get a new id
                Logger.log("inserting deleted item, trying to restore")
                cv.put("id", item.id)
            }
            cv.put("repeat_id", item.repeatID)
            return cv
        }


        fun getContentValues(repeat: Repeat?): ContentValues {
            Logger.log("DBAdmin.getContentValues(Repeat)")
            val cv = ContentValues()
            val gson = Gson()
            cv.put("json", gson.toJson(repeat))
            return cv
        }

        fun getContentValues(transaction: Transaction): ContentValues {
            val cv = ContentValues()
            cv.put("description", transaction.description)
            cv.put("amount", transaction.amount)
            cv.put("date", transaction.date.toEpochDay())
            return cv
        }

        fun getRepeat(cursor: Cursor): Repeat {
            if (VERBOSE) Logger.log("...getRepeat(Cursor)")
            val id = cursor.getLong(0)
            val json = cursor.getString(1)
            val repeat = Gson().fromJson(json, Repeat::class.java)
            repeat.id = id
            return repeat
        }


        /**
         * creates default lists/items
         * these being the ROOT of everything
         * saves root ids in sharedPrefs...
         * children to root: today, todo, projects, appointments, panic
         * @param context, context context context
         */
        @JvmStatic
        fun insertRootItems(context: Context?) {
            Logger.log("...insertRootItems(Context)")
            val settings = Settings.getInstance(context)
            val db = SqliteLocalDB(context)
            //create the root item
            var root = Item("root")
            root.setType(Type.ROOT)
            root = db.insert(root)
            settings.addRootID(Settings.Root.THE_ROOT, root.id, context)

            var todayRoot = App.getRootItem("today")
            var todoRoot = App.getRootItem("todo")
            var projectsRoot = App.getRootItem("projects")
            var appointmentsRoot = App.getRootItem("appointments")
            var panicRoot = App.getRootItem("panicList")
            appointmentsRoot = db.insertChild(root, appointmentsRoot)
            settings.addRootID(Settings.Root.APPOINTMENTS, appointmentsRoot.id, context)
            todayRoot = db.insertChild(root, todayRoot)
            settings.addRootID(Settings.Root.DAILY, todayRoot.id, context)
            projectsRoot = db.insertChild(root, projectsRoot)
            settings.addRootID(Settings.Root.PROJECTS, projectsRoot.id, context)
            todoRoot = db.insertChild(root, todoRoot)
            settings.addRootID(Settings.Root.TODO, todoRoot.id, context)
            panicRoot = db.insertChild(root, panicRoot)
            settings.addRootID(Settings.Root.PANIC, panicRoot.id, context)
            db.close()
        }

        fun listTables(context: Context?) {
            Logger.log("DBAdmin.listTables()")
            SqliteLocalDB(context).use { db ->
                db.tableNames.forEach(Consumer { x: String? -> println(x) })
            }
        }

        fun getTableNames(context: Context?): List<String> {
            Logger.log("DBAdmin.listTables()")
            SqliteLocalDB(context).use { db ->
                return db.tableNames
            }
        }

        fun addMentalColumnsToItemsTable(context: Context?) {
            Logger.log("DBAdmin.addMentalColumnsToItemsTable(Context)")
            try {
                SqliteLocalDB(context).use { db ->
                    db.executeSQL(Queeries.ADD_COLUMN_ANXIETY_TO_ITEMS)
                    Logger.log("column anxiety added")
                    db.executeSQL(Queeries.ADD_COLUMN_ENERGY_TO_ITEMS)
                    Logger.log("column energy added")
                    db.executeSQL(Queeries.ADD_COLUMN_MOOD_TO_ITEMS)
                    Logger.log("column mood added")
                    db.executeSQL(Queeries.ADD_COLUMN_STRESS_TO_ITEMS)
                    Logger.log("column stress created?")
                }
            } catch (e: Exception) {
                Logger.log("an exception occurred")
                e.printStackTrace()
            }
        }
    }
}


