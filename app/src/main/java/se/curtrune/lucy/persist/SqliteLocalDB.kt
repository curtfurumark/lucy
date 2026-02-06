package se.curtrune.lucy.persist

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import se.curtrune.lucy.activities.economy.classes.Asset
import se.curtrune.lucy.activities.economy.classes.Transaction
import se.curtrune.lucy.activities.economy.persist.ECDBAdmin
import se.curtrune.lucy.activities.economy.persist.EcQueeries
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.item.Repeat
import se.curtrune.lucy.util.Logger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Locale

/**
 * Manages the local SQLite database for the Lucy application.
 *
 * This class extends [SQLiteOpenHelper] to handle the creation, version management,
 * and basic CRUD (Create, Read, Update, Delete) operations for the application's data.
 * It provides methods to interact with various data models like [Item], [Repeat],
 * [Transaction], and [Asset].
 *
 * The database schema is defined and managed within this class, including tables for
 * items, repeating tasks, and financial records.
 *
 * @param context The context used to open or create the database.
 * @property dbName The name of the database file.
 * @property dbVersion The version of the database schema.
 */
class SqliteLocalDB(context: Context?) :
    SQLiteOpenHelper(context, dbName, null, dbVersion) {
    private var db: SQLiteDatabase? = null

    init {
        if (VERBOSE) Logger.log("SqliteLocalDB(Context)")
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        Logger.log("SqliteLocalDB.onCreate(SQLiteDatabase)")
        Logger.log("...creating table items", Queeries.CREATE_TABLE_ITEMS)
        sqLiteDatabase.execSQL(Queeries.CREATE_TABLE_ITEMS)
        Logger.log("...creating table repeat", Queeries.CREATE_TABLE_REPEAT)
        sqLiteDatabase.execSQL(Queeries.CREATE_TABLE_REPEAT)
        Logger.log("...creating transactions and assets  tables")
        sqLiteDatabase.execSQL(EcQueeries.CREATE_TABLE_TRANSACTIONS)
        sqLiteDatabase.execSQL(EcQueeries.CREATE_TABLE_ASSETS)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Logger.log("SqliteLocalDB.onUpgrade(SQLiteDatabase, int, int)")
        Logger.log(
            String.format(
                Locale.getDefault(),
                "oldVersion %d newVersion %d",
                oldVersion,
                newVersion
            )
        )
/*        sqLiteDatabase.execSQL(Queeries.CREATE_TABLE_REPEAT)
        sqLiteDatabase.execSQL(Queeries.DROP_TABLE_MENTAL)
        sqLiteDatabase.execSQL(Queeries.ADD_COLUMN_ANXIETY_TO_ITEMS)
        sqLiteDatabase.execSQL(Queeries.ADD_COLUMN_ENERGY_TO_ITEMS)
        sqLiteDatabase.execSQL(Queeries.ADD_COLUMN_MOOD_TO_ITEMS)
        sqLiteDatabase.execSQL(Queeries.ADD_COLUMN_STRESS_TO_ITEMS)
        sqLiteDatabase.execSQL(Queeries.ADD_COLUMN_REPEAT_ID_TO_ITEMS)*/
    }

    override fun close() {
        if (VERBOSE) Logger.log("SqliteLocalDB.close()")
        if (db != null && db!!.isOpen) {
            db!!.close()
            db = null
        }
    }

    fun getNumberChildren(id: Long): Int {
        Logger.log("SqliteLocalDB.getNumberChildren(long)", id)
        db = this.writableDatabase
        val query =
            String.format(Locale.getDefault(), "SELECT count(*) FROM items WHERE parentId = %d", id)
        //String queery = "SELECT count(*) FROM items WHERE parentId = %d"
        val cursor = db!!.rawQuery(query, null)
        cursor.moveToFirst()
        val numberOfChildren = cursor.getInt(0)
        Logger.log("...number of children be: ", numberOfChildren)
        cursor.close()
        db!!.close()
        return numberOfChildren
    }

    fun getNumberChildren(id: Long, db: SQLiteDatabase): Int {
        Logger.log("SqliteLocalDB.getNumberChildren(long, SQLiteDataBase)", id)
        val query =
            String.format(Locale.getDefault(), "SELECT count(*) FROM items WHERE parentId = %d", id)
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val numberOfChildren = cursor.getInt(0)
        Logger.log("number of children be: ", numberOfChildren)
        cursor.close()
        return numberOfChildren
    }

    fun delete(item: Item): Int {
        println("SqliteLocalDB.delete(Item $item.heading)")
        db = this.writableDatabase
        val whereClause = String.format(Locale.getDefault(), "id = %d", item.id)
        println("delete ...where: $whereClause")
        val rowsAffected = db!!.delete(ITEMS_TABLE, whereClause, null)
        if (rowsAffected != 1) {
            println("some kind of error deleting item: ${item.heading}, id: ${item.id}")
        } else {
            Logger.log("...item deleted")
            if (getNumberChildren(item.parentId) == 0) { // we have just deleted the last child'
                Logger.log("...lastChild just deleted, setting parent has child to false")
                setItemHasChild(item.parentId, false)
            }
        }
        db!!.close()
        return rowsAffected
    }

    fun delete(id: Long): Int {
        if (VERBOSE) Logger.log("SqliteLocalDB.delete(Item)")
        db = this.writableDatabase
        val whereClause = String.format(Locale.getDefault(), "id = %d", id)
        val rowsAffected = db!!.delete(ITEMS_TABLE, whereClause, null)
        if (rowsAffected != 1) {
            Logger.log("some kind of error deleting item")
        } /*else {
            if( getNumberChildren(item.getParentId()) == 0){ // we have just deleted the last child'
                log("...lastChild just deleted, setting parent has child to false");
                setItemHasChild(item.getParentId(), false) ;
            }
        }*/
        db!!.close()
        return rowsAffected
    }

    fun executeSQL(sql: String?) {
        Logger.log("SqliteLocalDB.executeSQL(String sql)", sql)
        db = this.writableDatabase
        try {
            db!!.execSQL(sql)
            Logger.log("...sql executed")
        } catch (exception: SQLException) {
            exception.printStackTrace()
            Logger.log(exception.message)
        }
        db!!.close()
    }

    fun getParent(item: Item): Item? {
        if (VERBOSE) Logger.log("SqliteLocalDB.getParent(Item)")
        return selectItem(item.parentId)
    }

    val tableNames: List<String>
        get() {
            if (VERBOSE) Logger.log("SqliteLocalDB.getTableNames()")
            val tableNames: MutableList<String> =
                ArrayList()
            val query =
                "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name"
            db = this.readableDatabase
            val cursor = db!!.rawQuery(query, null)
            if (cursor.moveToFirst()) {
                do {
                    val table_name = cursor.getString(0)
                    tableNames.add(table_name)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db!!.close()
            return tableNames
        }

    fun getChildren(item: Item?): List<Item> {
        return selectItems(Queeries.selectChildren(item))
    }

    /**
     * insert the item into the local database
     * this method also inserts this items mental,
     * if no item does not have a mental instance, the method creates a zero mental
     * @param item, the item to be inserted
     * @return the inserted item, with its id set to whatever autoincrement
     */
    fun insert(item: Item): Item {
        if (VERBOSE) Logger.log("SqliteLocalDB.insert(Item)", item.heading)
        if (VERBOSE) Logger.log(item)
        db = this.writableDatabase
        val id = db!!.insert(ITEMS_TABLE, null, DBAdmin.getContentValues(item))
        if (id == -1L) {
            Logger.log("ERROR: SqliteLocalDB.insert(Item) insert returned -1, not good enough")
        } else if (VERBOSE) {
            if (VERBOSE) Logger.log("...item inserted with id ", id)
            Logger.log(item)
        }
        item.id = id.toLong()
        db!!.close()
        return item
    }

    fun insert(items: List<Item>) {
        Logger.log("SqliteLocalDB.insert(List<Item>)")
        db = this.writableDatabase
        for (item in items) {
            val id = db!!.insert(ITEMS_TABLE, null, DBAdmin.getContentValues(item))
            if (id == -1L) {
                Logger.log("ERROR inserting item", item.heading)
            } else {
                item.id = id
            }
        }
    }

    fun insert(repeat: Repeat): Repeat? {
        Logger.log("SqliteLocalDB.insert(Repeat)")
        db = this.writableDatabase
        val id = db!!.insert(TABLE_REPEAT, null, DBAdmin.getContentValues(repeat))
        if (id == -1L) {
            Logger.log("ERROR  inserting repeat")
            return null
        }
        repeat.templateID = id
        return repeat
    }

    fun insert(transaction: Transaction): Transaction? {
        if (VERBOSE) Logger.log("SqliteLocalDB.insert(Transaction)")
        db = this.writableDatabase
        val id = db!!.insert(TABLE_TRANSACTIONS, null, DBAdmin.getContentValues(transaction))
        if (id != -1L) {
            transaction.id = id
        } else {
            Logger.log("ERROR inserting transaction")
            return null
        }
        return transaction
    }

    fun insertChild(parent: Item, child: Item): Item {
        var child = child
        if (VERBOSE) Logger.log("SqliteLocalDB.insertChild(Item, Item)")
        if (VERBOSE) Logger.log("\tparent", parent.heading)
        if (VERBOSE) Logger.log("\tchild", child.heading)
        child.parentId = parent.id
        db = this.writableDatabase
        child = insert(child)
        if (!parent.hasChild()) {
            Logger.log("...parent has no child, will set parent has child to true")
            setItemHasChild(parent.id, true)
        }
        db!!.close()
        return child
    }

    fun open() {
        if (VERBOSE) Logger.log("SqliteLocalDB.open()")
        db = this.readableDatabase
    }

    fun restore(item: Item) {
        db = this.writableDatabase
        db!!.insert(ITEMS_TABLE, null, DBAdmin.getContentValues(item))
    }

    fun selectAssets(queery: String): List<Asset> {
        if (VERBOSE) Logger.log("SqliteLocalDB.selectAssets()")
        db = this.readableDatabase
        val assets: MutableList<Asset> = ArrayList()
        val cursor = db!!.rawQuery(queery, null)
        if (cursor.moveToFirst()) {
            do {
                val asset = ECDBAdmin.getAsset(cursor)
                assets.add(asset)
            } while (cursor.moveToNext())
        }
        db!!.close()
        return assets
    }


    /**
     * this is the one, that actually gets a list of items
     * @param query, the query to be executed
     * @return a list of items, matched by the query
     */
    fun selectItems(query: String): List<Item> {
        if (VERBOSE) println("SqliteLocalDB.selectItems($query)")
        db = this.readableDatabase
        val items: MutableList<Item> = ArrayList()
        val cursor = db!!.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val item = DBAdmin.getItem(cursor)
                items.add(item)
            } while (cursor.moveToNext())
        }
        db!!.close()
        return items
    }


    fun selectItems(date: LocalDate, context: Context?): List<Item> {
        if (VERBOSE) Logger.log("SqliteLocalDB.selectItems(LocalDate) ", date.toString())
        var items: List<Item>
        db = this.readableDatabase
        items = selectItems(Queeries.selectItems(date))
        db!!.close()
        //Autoclosable added to VERSION.0 (10)
/*        SqliteLocalDB(context).use { db ->
            items = db.selectItems(Queeries.selectItems(date))
        }*/
        return items
    }


    fun selectItems(): List<Item> {
        if (VERBOSE) Logger.log("SqliteLocalDB.selectItems()")
        return selectItems(Queeries.selectItems())
    }

    /**
     * selects and item
     * @param id, the id of the item to be selected
     * @return the item with aforementioned id, or null if there's no such item
     */
    fun selectItem(id: Long): Item? {
        if (VERBOSE) Logger.log("...selectItem(long id) ", id)
        val query = Queeries.selectItem(id)
        var item: Item? = null
        db = this.readableDatabase
        val cursor = db!!.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            item = DBAdmin.getItem(cursor)
        } else {
            Logger.log("...WARNING, no item with id ", id)
        }
        cursor.close()
        db!!.close()
        return item
    }

    fun selectRepeat(id: Long): Repeat? {
        if (VERBOSE) Logger.log("...selectRepeat(long id) ", id)
        val query = Queeries.selectRepeat(id)
        var repeat: Repeat? = null
        db = this.readableDatabase
        val cursor = db!!.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            repeat = DBAdmin.getRepeat(cursor)
        } else {
            Logger.log("...WARNING, no item with id ", id)
        }
        cursor.close()
        db!!.close()
        return repeat
    }

    fun selectRepeats(): List<Repeat> {
        Logger.log("SqliteLocalDB.selectRepeats(Context)")
        val repeats: MutableList<Repeat> = ArrayList()
        val queery = "SELECT * FROM repeat"
        db = this.readableDatabase
        val cursor = db!!.rawQuery(queery, null)
        if (cursor.moveToFirst()) {
            do {
                repeats.add(DBAdmin.getRepeat(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db!!.close()
        return repeats
    }

    /**
     * @param id, of the item which to set field hasChild
     * @return true if operation succeeded
     */
    fun setItemHasChild(id: Long, hasChild: Boolean): Boolean {
        Logger.log("...setItemHasChild(long id)", id)
        db = this.writableDatabase
        val whereClause = String.format(Locale.ENGLISH, "id = %d", id)
        val cv = ContentValues()
        cv.put("hasChild", if (hasChild) 1 else 0)
        cv.put("updated", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
        val rowsAffected = db!!.update(ITEMS_TABLE, cv, whereClause, null)
        if (rowsAffected != 1) {
            Logger.log("ERROR setItemHasChild...")
        }
        db!!.close()
        return rowsAffected == 1
    }

    fun touch(item: Item) {
        println("SqliteLocalDB.touch(${item.heading})")
        db = this.writableDatabase
        val cv = ContentValues()
        cv.put("updated", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
        val whereClause = String.format(Locale.getDefault(), "id = %d", item.id)
        val rowsAffected = db!!.update(ITEMS_TABLE, cv, whereClause, null)
        Logger.log("...rowsAffected", rowsAffected)
    }

    /**
     * touches parents all the way to the topmost parent
     * @param item, the item whose parents needs to be updated
     */
    fun touchParents(item: Item) {
        Logger.log("SqliteLocalDB.touchParents(Item)", item.heading)
        var child = item
        var parent = getParent(child)
        while(parent != null){
            touch(parent)
            child = parent
            parent = getParent(child)
        }
    }

    /**
     * updates item and mental if it has any
     * @param item, the item to be updated
     * @return returns 1 if successful
     */
    fun update(item: Item): Int {
        if (VERBOSE) println("SqliteLocalDB.update($item)")
        db = this.writableDatabase
        val whereClause = String.format(Locale.getDefault(), "id = %d", item.id)
        val rowsAffected = db!!.update(ITEMS_TABLE, DBAdmin.getContentValues(item), whereClause, null)
        if(VERBOSE )("... rowsAffected: $rowsAffected")
        db!!.close()
        return rowsAffected
    }

    fun selectTransactions(queery: String): List<Transaction> {
        Logger.log("SqliteLocalDB.selectTransactions(String)", queery)
        db = this.readableDatabase
        val transactions: MutableList<Transaction> = ArrayList()
        val cursor = db!!.rawQuery(queery, null)
        if (cursor.moveToFirst()) {
            do {
                transactions.add(ECDBAdmin.getTransaction(cursor))
            } while (cursor.moveToNext())
        }
        db!!.close()
        cursor.close()
        return transactions
    }

    /**
     * @param asset the asset to be inserted into the database
     * @return asset with db id or null if action for some stupid reason failed
     */
    fun insert(asset: Asset): Asset {
        //var asset = asset
        Logger.log("SqliteLocalDB.insert(Asset)")
/*        db = this.writableDatabase
        val id = db!!.insert(TABLE_ASSETS, null, ECDBAdmin.getContentValues(asset))
        if (id != -1L) {
            asset.id = id
        } else {
            Logger.log("ERROR inserting asset", asset.account)
            asset = null
        }*/
        return asset
    }


    fun update(repeat: Repeat): Int {
        if (VERBOSE) Logger.log("SqliteLocalDB.update(Repeat)")
        if (VERBOSE) Logger.log(repeat.toString())
        db = this.writableDatabase
        val whereClause = String.format(Locale.getDefault(), "id = %d", repeat.templateID)
        val rowsAffected =
            db!!.update(TABLE_REPEAT, DBAdmin.getContentValues(repeat), whereClause, null)
        Logger.log("...update item ok: ", rowsAffected == 1)
        db!!.close()
        return rowsAffected
    }

    fun getColumns(tableName: String) {
        Logger.log("SqliteLocalDB.getColumns(String)", tableName)
        //String queery = String.format(Locale.getDefault(),"SELECT sql FROM sqlite_master WHERE tbl_name = '%s' AND type = 'table'", tableName);
        //String queery = String.format(Locale.getDefault(), "PRAGMA table_info(%s)", tableName);
        val queery = String.format(Locale.getDefault(), "SELECT * FROM %s LIMIT 1", tableName)
        Logger.log(queery)
        db = this.readableDatabase
        val cursor = db!!.rawQuery(queery, null, null)
        //cursor.moveToFirst();
        if (cursor == null) {
            Logger.log("...cursor is null, returning")
            return
        }
        while (cursor.moveToNext()) {
            val columnNames = cursor.columnNames
            for (i in columnNames.indices) {
                val name = columnNames[i]
                val type = cursor.getType(i)
                val strColumnInfo =
                    String.format(Locale.getDefault(), "name[%d] %s: %d", i, name, type)
                Logger.log(strColumnInfo)
            }
        }
    }

    companion object {


        const val dbName: String = "lucy.db"
        private const val ITEMS_TABLE = "items"

        private const val TABLE_TRANSACTIONS = "transactions"
        private const val TABLE_ASSETS = "assets"
        private const val TABLE_REPEAT = "repeat"
        const val dbVersion: Int = 2
        var VERBOSE: Boolean = false
    }
}

