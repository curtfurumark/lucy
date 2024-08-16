package se.curtrune.lucy.persist;

import static se.curtrune.lucy.util.Logger.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;

public class LocalDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "lucy.db";
    private static final String ITEMS_TABLE = "items";
    private static final String TABLE_MENTAL = "mental";
    //private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String TABLE_ASSETS = "assets";
    private static final int DB_VERSION = 1;
    public static boolean VERBOSE = false;

    private SQLiteDatabase db;

    public LocalDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        log("LocalDB.onCreate(SQLiteDatabase)");
        log("...creating table items");
        sqLiteDatabase.execSQL(Queeries.CREATE_TABLE_ITEMS);
        log("....creating table mental;");
        sqLiteDatabase.execSQL(Queeries.CREATE_TABLE_MENTAL);
        log("...three tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

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

    public int delete(Mental mental) {
        if( VERBOSE) log("LocalDb.delete(MentalType)");
        db = this.getWritableDatabase();
        String whereClause = String.format(Locale.getDefault(), "id =%d", mental.getID());
        int rowsAffected = db.delete(TABLE_MENTAL, whereClause, null);
        db.close();
        return rowsAffected;
    }

    public void executeSQL(String sql) {
        log("LocalDB.executeSQL(String sql)", sql);
        db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public Item getParent(Item item) {
        if( VERBOSE) log("LocalDB.getParent(Item)");
        return selectItem(item.getParentId());
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
        } else {
            if( VERBOSE) log("...item inserted with id ", id);
        }
        item.setId((int) id);
        Mental mental = item.getMental();
        if( mental == null){
            log("WARNING, inserting an item with no mental...");
            mental = new Mental(item);
        }
        mental.setHeading(item.getHeading());
        mental.setTime(item.getTargetTime());
        mental.setDate(item.getTargetDate());
        mental.isDone(item.isDone());
        mental.setIsTemplate(item.isTemplate());
        mental.setItemID(item.getID());
        mental = insert(mental);
        item.setMental(mental);
        db.close();
        return item;
    }

    /**
     * inserts a  mental object into the mentals table
     * @param mental, the mental to be inserted
     * @return the inserted mental, but now with a valid id
     */
    public Mental insert(Mental mental) {
        if( VERBOSE) log("LocalDB.insert(MentalType)");
        db = this.getWritableDatabase();
        long id = db.insert(TABLE_MENTAL, null, DBAdmin.getContentValues(mental));
        if (id == -1) {
            log("ERROR, inserting mental");
            mental = null;
        }else{
            mental.setID(id);
        }
        db.close();
        return mental;
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
                Mental mental = selectMental(Queeries.selectMental(item));
                if( mental == null){
                    mental = new Mental(item);
                }
                item.setMental(mental);
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
            log("...WARNING, not item with id ", id);
        }
        cursor.close();
        db.close();
        return item;
    }

    /**
     * @param query the query to be executed
     * @return MentalType if there is such a thing or null if not found
     */
    public Mental selectMental(String query) {
        if (VERBOSE) log("LocalDB.selectMentals(String)", query);
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Mental mental = null;
        if (cursor.moveToFirst()) {
            mental = DBAdmin.getMental(cursor);
        }
        db.close();
        cursor.close();
        return mental;
    }

    public List<Mental> selectMentals(String query) {
        if( VERBOSE) log("LocalDB.selectMentals(String)", query);
        List<Mental> items = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                items.add(DBAdmin.getMental(cursor));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return items;
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
        Mental mental = item.getMental();
        mental.isDone(item.isDone());
        if( VERBOSE) log(mental);
        rowsAffected = update(item.getMental());
        if( VERBOSE) log("...update mental ok: ", rowsAffected == 1);
        db.close();
        return rowsAffected;
    }

    public int update(Mental mental) {
        log("LocalDB.update(MentalType)");
        log(mental);
        db = this.getWritableDatabase();
        String whereClause = String.format(Locale.getDefault(), "id = %d", mental.getID());
        int rowsAffected = db.update(TABLE_MENTAL, DBAdmin.getContentValues(mental), whereClause, null);
        log("...rowsAffected", rowsAffected);
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
}

