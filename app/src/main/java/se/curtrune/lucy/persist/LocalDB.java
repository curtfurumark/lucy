package se.curtrune.lucy.persist;

import static se.curtrune.lucy.util.Logger.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.activities.economy.classes.Asset;
import se.curtrune.lucy.activities.economy.classes.Transaction;
import se.curtrune.lucy.activities.economy.persist.ECDBAdmin;
import se.curtrune.lucy.activities.economy.persist.EcQueeries;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.Repeat;

public class LocalDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "lucy.db";
    private static final String ITEMS_TABLE = "items";

    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String TABLE_ASSETS = "assets";
    private static final String TABLE_REPEAT = "repeat";
    private static final int DB_VERSION = 2;
    public static boolean VERBOSE = false;

    private SQLiteDatabase db;

    public LocalDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if( VERBOSE) log("LocalDB(Context)");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        log("LocalDB.onCreate(SQLiteDatabase)");
        log("...creating table items", Queeries.CREATE_TABLE_ITEMS);
        sqLiteDatabase.execSQL(Queeries.CREATE_TABLE_ITEMS);
        log("...creating table repeat", Queeries.CREATE_TABLE_REPEAT);
        sqLiteDatabase.execSQL(Queeries.CREATE_TABLE_REPEAT);
        log("...creating transactions and assets  tables");
        sqLiteDatabase.execSQL(EcQueeries.CREATE_TABLE_TRANSACTIONS);
        sqLiteDatabase.execSQL(EcQueeries.CREATE_TABLE_ASSETS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        log("LocalDB.onUpgrade(SQLiteDatabase, int, int)");
        log(String.format(Locale.getDefault(), "oldVersion %d newVersion %d", oldVersion, newVersion));
        sqLiteDatabase.execSQL(Queeries.CREATE_TABLE_REPEAT);
        sqLiteDatabase.execSQL(Queeries.DROP_TABLE_MENTAL);
        sqLiteDatabase.execSQL(Queeries.ADD_COLUMN_ANXIETY_TO_ITEMS);
        sqLiteDatabase.execSQL(Queeries.ADD_COLUMN_ENERGY_TO_ITEMS);
        sqLiteDatabase.execSQL(Queeries.ADD_COLUMN_MOOD_TO_ITEMS);
        sqLiteDatabase.execSQL(Queeries.ADD_COLUMN_STRESS_TO_ITEMS);
        sqLiteDatabase.execSQL(Queeries.ADD_COLUMN_REPEAT_ID_TO_ITEMS);
    }

    public void close() {
        if( VERBOSE) log("LocalDB.close()");
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
        }
    }

    public int delete(Item item) {
        if( VERBOSE) log("LocalDB.delete(Item)");
        db = this.getWritableDatabase();
        String whereClause = String.format(Locale.getDefault(), "id = %d", item.getID());
        int rowsAffected = db.delete(ITEMS_TABLE, whereClause, null);
        if (rowsAffected != 1) {
            log("some kind of error deleting item");
        }
        db.close();
        return rowsAffected;
    }

    public void executeSQL(String sql) {
        log("LocalDB.executeSQL(String sql)", sql);
        db = this.getWritableDatabase();
        try {
            db.execSQL(sql);
            log("...sql executed");
        }catch (SQLException exception){
            exception.printStackTrace();
            log(exception.getMessage());
        }
        db.close();
    }

    public Item getParent(Item item) {
        if( VERBOSE) log("LocalDB.getParent(Item)");
        return selectItem(item.getParentId());
    }
    public static String getDbName(){
        return DB_NAME;
    }
    public List<String> getTableNames() {
        if( VERBOSE) log("LocalDB.getTableNames()");
        List<String> tableNames = new ArrayList<>();
        String query = "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String table_name = cursor.getString(0);
                tableNames.add(table_name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tableNames;
    }

    public List<Item> getChildren(Item item) {
        return selectItems(Queeries.selectChildren(item));
    }
    public static int getDbVersion(){
        return DB_VERSION;
    }

    /**
     * insert the item into the local database
     * this method also inserts this items mental,
     * if no item does not have a mental instance, the method creates a zero mental
     * @param item, the item to be inserted
     * @return the inserted item, with its id set to whatever autoincrement
     */

    public Item insert(Item item) {
        if( VERBOSE) log("LocalDB.insert(Item)", item.getHeading());
        if (VERBOSE) log(item);
        db = this.getWritableDatabase();
        long id = db.insert(ITEMS_TABLE, null, DBAdmin.getContentValues(item));
        if (id == -1) {
            log("ERROR: LocalDB.insert(Item) insert returned -1, not good enough");
        } else if( VERBOSE){
            if( VERBOSE) log("...item inserted with id ", id);
            log(item);
        }
        item.setId((int) id);
        db.close();
        return item;
    }
    public void insert(List<Item> items) {
        log("LocalDB.insert(List<Item>)");
        db = this.getWritableDatabase();
        for(Item item: items){
            long id = db.insert(ITEMS_TABLE, null, DBAdmin.getContentValues(item));
            if( id == -1){
                log("ERROR inserting item", item.getHeading());
            }else {
                item.setId(id);
            }
        }
    }

    public Repeat insert(Repeat repeat){
        log("LocalDB.insert(Repeat)");
        db = this.getWritableDatabase();
        long id = db.insert(TABLE_REPEAT, null, DBAdmin.getContentValues(repeat));
        if(id == -1){
            log("ERROR  inserting repeat");
            return null;
        }
        repeat.setID(id);
        return repeat;

    }

    public Transaction insert(Transaction transaction) {
        if( VERBOSE) log("LocalDB.insert(Transaction)");
        db = this.getWritableDatabase();
        long id = db.insert(TABLE_TRANSACTIONS, null, DBAdmin.getContentValues(transaction));
        if (id != -1) {
            transaction.setID(id);
        } else {
            log("ERROR inserting transaction");
            return null;
        }
        return transaction;
    }

    public Item insertChild(Item parent, Item child) {
        if( VERBOSE) log("LocalDB.insertChild(Item, Item)");
        if(VERBOSE) log("\tparent", parent.getHeading());
        if(VERBOSE) log("\tchild", child.getHeading());
        child.setParentId(parent.getID());
        db = this.getWritableDatabase();
        child = insert(child);
        if (!parent.hasChild()) {
            log("...parent has no child, will set parent has child to true");
            setItemHasChild(parent.getID(), true);
        }
        db.close();
        return child;
    }

    public void open() {
        if( VERBOSE) log("LocalDB.open()");
        db = this.getReadableDatabase();
    }


    public List<Asset> selectAssets(String queery) {
        if( VERBOSE) log("LocalDB.selectAssets()");
        db = this.getReadableDatabase();
        List<Asset> assets = new ArrayList<>();
        Cursor cursor = db.rawQuery(queery, null);
        if (cursor.moveToFirst()) {
            do {
                Asset asset = ECDBAdmin.getAsset(cursor);
                assets.add(asset);
            } while (cursor.moveToNext());
        }
        db.close();
        return assets;
    }


    /**
     * this is the one, that actually gets a list of items
     * @param query, the query to be executed
     * @return a list of items, matched by the query
     */
    public List<Item> selectItems(String query) {
        if( VERBOSE) log("LocalDB.selectItems(String query)", query);
        db = this.getReadableDatabase();
        List<Item> items = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Item item = DBAdmin.getItem(cursor);
                items.add(item);
            } while (cursor.moveToNext());
        }
        db.close();
        return items;
    }


    public List<Item> selectItems(LocalDate date, Context context) {
        if( VERBOSE) log("LocalDB.selectItems(LocalDate) ", date.toString());
        List<Item> items;
        try(LocalDB db = new LocalDB(context)){
            items = db.selectItems(Queeries.selectItems(date));
        }
        return items;
    }


    public List<Item> selectItems() {
        if( VERBOSE) log("LocalDB.selectItems()");
        return selectItems(Queeries.selectItems());
    }

    /**
     * selects and item
     * @param id, the id of the item to be selected
     * @return the item with aforementioned id, or null if there's no such item
     */
    public Item selectItem(long id) {
        if( VERBOSE) log("...selectItem(long id) ", id);
        String query = Queeries.selectItem(id);
        Item item = null;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            item = DBAdmin.getItem(cursor);
        } else {
            log("...WARNING, no item with id ", id);
        }
        cursor.close();
        db.close();
        return item;
    }

    public Repeat selectRepeat(long id) {
        if( VERBOSE) log("...selectRepeat(long id) ", id);
        String query = Queeries.selectRepeat(id);
        Repeat repeat = null;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            repeat = DBAdmin.getRepeat(cursor);
        } else {
            log("...WARNING, no item with id ", id);
        }
        cursor.close();
        db.close();
        return repeat;
    }
    public List<Repeat> selectRepeats(){
        log("LocalDB.selectRepeats(Context)");
        List<Repeat> repeats = new ArrayList<>();
        String queery = "SELECT * FROM repeat";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queery, null);
        if(cursor.moveToFirst()){
            do {
                repeats.add(DBAdmin.getRepeat(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return repeats;
    }

    /**
     * @param id, of the item which to set field hasChild
     * @return true if operation succeeded
     */
    public boolean setItemHasChild(long id, boolean hasChild) {
        log("...setItemHasChild(long id)", id);
        db = this.getWritableDatabase();
        String whereClause = String.format(Locale.ENGLISH, "id = %d", id);
        ContentValues cv = new ContentValues();
        cv.put("hasChild", hasChild ? 1 : 0);
        cv.put("updated", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        int rowsAffected = db.update(ITEMS_TABLE, cv, whereClause, null);
        if (rowsAffected != 1) {
            log("ERROR setItemHasChild...");
        }
        db.close();
        return rowsAffected == 1;
    }

    public void touch(Item item) {
        log("LocalDB.touch(Item)");
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("updated", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        String whereClause = String.format(Locale.getDefault(), "id = %d", item.getID());
        int rowsAffected = db.update(ITEMS_TABLE, cv, whereClause, null);
        log("...rowsAffected", rowsAffected);
    }

    /**
     * touches parents all the way to the topmost parent
     * @param item, the item whose parents needs to be updated
     */

    public void touchParents(Item item) {
        log("LocalDB.touchParents(Item)");
        Item parent;
        Item child = item;
        while ((parent = getParent(child)) != null) {
            touch(parent);
            child = parent;
        }
    }

    /**
     * updates item and mental if it has any
     * @param item, the item to be updated
     * @return returns 1 if successful
     */
    public int update(Item item) {
        if( VERBOSE) log("LocalDB.update(Item)", item.getHeading());
        if( VERBOSE) log(item);
        db = this.getWritableDatabase();
        String whereClause = String.format(Locale.getDefault(), "id = %d", item.getID());
        int rowsAffected = db.update(ITEMS_TABLE, DBAdmin.getContentValues(item), whereClause, null);
        log("...update item ok: ", rowsAffected == 1);
        db.close();
        return rowsAffected;
    }

    public List<Transaction> selectTransactions(String queery) {
        log("LocalDB.selectTransactions(String)", queery);
        db = this.getReadableDatabase();
        List<Transaction> transactions = new ArrayList<>();
        Cursor cursor = db.rawQuery(queery, null);
        if (cursor.moveToFirst()) {
            do {
                transactions.add(ECDBAdmin.getTransaction(cursor));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return transactions;
    }

    /**
     * @param asset the asset to be inserted into the database
     * @return asset with db id or null if action for some stupid reason failed
     */
    public Asset insert(Asset asset) {
        log("LocalDB.insert(Asset)");
        db = this.getWritableDatabase();
        long id = db.insert(TABLE_ASSETS, null, ECDBAdmin.getContentValues(asset));
        if (id != -1) {
            asset.setID(id);
        } else {
            log("ERROR inserting asset", asset.getAccount());
            asset = null;
        }
        return asset;
    }


    public int update(Repeat repeat){
        if( VERBOSE) log("LocalDB.update(Repeat)");
        if( VERBOSE) log(repeat);
        db = this.getWritableDatabase();
        String whereClause = String.format(Locale.getDefault(), "id = %d", repeat.getID());
        int rowsAffected = db.update(TABLE_REPEAT, DBAdmin.getContentValues(repeat), whereClause, null);
        log("...update item ok: ", rowsAffected == 1);
        db.close();
        return rowsAffected;
    }

    public void getColumns(String tableName) {
        log("LocalDB.getColumns(String)", tableName);
        //String queery = String.format(Locale.getDefault(),"SELECT sql FROM sqlite_master WHERE tbl_name = '%s' AND type = 'table'", tableName);
        //String queery = String.format(Locale.getDefault(), "PRAGMA table_info(%s)", tableName);
        String queery = String.format(Locale.getDefault(),"SELECT * FROM %s LIMIT 1", tableName);
        log(queery);
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queery, null, null);
        //cursor.moveToFirst();
        if(cursor == null){
            log("...cursor is null, returning");
            return;
        }
        while(cursor.moveToNext()) {
            String[] columnNames = cursor.getColumnNames();
            for (int i = 0; i < columnNames.length; i++) {
                String name = columnNames[i];
                int type = cursor.getType(i);
                String strColumnInfo = String.format(Locale.getDefault(), "name[%d] %s: %d", i, name, type);
                log(strColumnInfo);
            }
        }
    }
}

