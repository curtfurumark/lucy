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
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.app.Lucinda;

public class LocalDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "lucy.db";
    private static final String ITEMS_TABLE = "items";
    private static final String TABLE_MENTAL = "mental";
    private static final String TABLE_CATEGORIES = "categories";
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
        log("...creating table categories");
        sqLiteDatabase.execSQL(Queeries.CREATE_TABLE_CATEGORIES);
        log("....creating table mental;");
        sqLiteDatabase.execSQL(Queeries.CREATE_TABLE_MENTAL);
        log("...three tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void close() {
        log("LocalDB.close()");
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
        }
    }

    public int delete(Item item) {
        log("LocalDB.delete(Item)");
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
        log("LocalDb.delete(Mental)");
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
        log("LocalDB.getParent(Item)");
        return selectItem(item.getParentId());
    }

    public String[] getCategories() {
        log("LocalDB.getCategories()");
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Queeries.selectCategories(), null);
        String[] categoriesArray = new String[cursor.getCount()];
        int index = 0;
        if (cursor.moveToFirst()) {
            do {
                String category = DBAdmin.getCategory(cursor);
                categoriesArray[index++] = category;
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return categoriesArray;
    }

    public List<String> getTableNames() {
        log("DBSQLite.getTableNames()");
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
        return null;
    }

    public Item getTree(Item root) {
        log("...getTree()");
        if (root.hasChild()) {
            List<Item> children = getChildren(root);
            root.setChildren(getChildren(root));
        }
        return null;
    }

    public Item insert(Item item) {
        log("LocalDB.insert(Item)", item.getHeading());
        if (VERBOSE) log(item);
        db = this.getWritableDatabase();
        //long id = db.insertOrThrow(ITEMS_TABLE, null,DBAdmin.getContentValues(item));
        long id = db.insert(ITEMS_TABLE, null, DBAdmin.getContentValues(item));
        if (id == -1) {
            log("...return id -1, not good enough");
        } else {
            log("...item inserted with id ", id);
        }
        item.setId((int) id);
        db.close();
        return item;
    }
/*    public Item insertChild(Item parent, Item child){
        log("...insertChild(Item, Item, Context");
        if( !parent.hasChild()){
            setItemHasChild(parent.getID(), true);
        }
        insert(child);
    }*/

    public Mental insert(Mental mental) {
        log("LocalDB.insert(Mental)");
        db = this.getWritableDatabase();
        //long id = db.insertOrThrow(TABLE_MENTAL, null, DBAdmin.getContentValues(mental));
        long id = db.insert(TABLE_MENTAL, null, DBAdmin.getContentValues(mental));
        if (id == -1) {
            log("ERROR, inserting mental");
        }
        db.close();
        mental.setID(id);
        return mental;
    }

    public Transaction insert(Transaction transaction) {
        log("LocalDB.insert(Transaction)");
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

    public void insertCategory(String category) {
        log("LocalDB.insertCategory(String)", category);
        db = this.getWritableDatabase();
        db.insert(TABLE_CATEGORIES, null, DBAdmin.getContentValues(category));
        db.close();
    }

    public Item insertChild(Item parent, Item child) {
        log("LocalDB.insertChild(Item, Item)");
        log("\tparent", parent.getHeading());
        log("\tchild", child.getHeading());
        child.setParentId(parent.getID());
        db = this.getWritableDatabase();
        long res = db.insert(ITEMS_TABLE, null, DBAdmin.getContentValues(child));
        if (res == -1) {
            log("ERROR inserting child");
            db.close();
            return null;
        } else {
            child.setId(res);
        }
        if (!parent.hasChild()) {
            log("...parent has no child");
            setItemHasChild(parent.getID(), true);
        }
        db.close();
        return child;
    }

    public void open() {
        log("LocalDB.open()");
        db = this.getReadableDatabase();
    }


    public List<Asset> selectAssets(String queery) {
        log("LocalDB.selectAssets()");
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

    public List<Item> selectChildren(Item parent) {
        log("LocalDB.selectChildren(Item)", parent != null ? parent.getHeading() : "parent id: 0");
        String queery = Queeries.selectChildren(parent);
        return selectItems(queery);
    }

    public List<Item> selectItems(LocalDate date, State state) {
        log("LocalDB.selectItems(LocalDate, State)", date.toString() + ", " + state.toString());
        String queery = Queeries.selectItems(date, state);
        return selectItems(queery);
    }

    public List<Item> selectItems(LocalDate date) {
        return selectItems(Queeries.selectItems(date));
    }

    public List<Item> selectItems(String query) {
        log("LocalDB.selectItems(String query)", query);
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


    public List<Mental> selectMentals() {
        if (VERBOSE) log("LocalDB.selectMentals()");
        String query = Queeries.selectMentals();
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

    public List<Mental> selectMentals2() {
        if (VERBOSE) log("LocalDB.selectMentals2()");
        String query = Queeries.selectMentals2();
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


    public List<Item> selectItems(LocalDate date, Context context) {
        log("LocalDB.selectItems(LocalDate) ", date.toString());
        LocalDB db = new LocalDB(context);
        return db.selectItems(Queeries.selectItems(date));
    }

    public List<Item> selectItems(Type type) {
        log("LocalDB.selectItems(Type)");
        return selectItems(Queeries.selectItems(type));

    }


    public List<Item> selectItems() {
        log("LocalDB.selectItems()");
        return selectItems(Queeries.selectItems());
    }

    public Item selectItem(long id) {
        log("...selectItem(long id) ", id);
        String query = Queeries.selectItem(id);
        Item item = null;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            item = DBAdmin.getItem(cursor);
        } else {
            log("...error, no item with id", id);
        }
        cursor.close();
        db.close();
        return item;
    }

    /**
     * @param query the query to be executed
     * @return Mental if there is such a thing or null if not found
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
        log("...selectMentals(String)", query);
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

    public List<Mental> selectMentalsFromItem(String query) {
        log("...selectMentalsFromItem(String)", query);
        List<Mental> items = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                items.add(DBAdmin.getMentalFromItem(cursor));
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
        String whereClause = String.format("id = %d", item.getID());
        int rowsAffected = db.update(ITEMS_TABLE, cv, whereClause, null);
        log("...rowsAffected", rowsAffected);
    }

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
     *
     * @param item
     * @return
     */
    public int update(Item item) {
        log("LocalDB.update(Item)", item.getHeading());
        log(item);
        db = this.getWritableDatabase();
        String whereClause = String.format("id = %d", item.getID());
        int rowsAffected = db.update(ITEMS_TABLE, DBAdmin.getContentValues(item), whereClause, null);
        log("...update ok: ", rowsAffected == 1);
        db.close();
        if (item.hasMental()) {
            update(item.getMental());
        }
        return rowsAffected;
    }

    public int update(Mental mental) {
        log("LocalDB.update(Mental)");
        log(mental);
        db = this.getWritableDatabase();
        String whereClause = String.format("id = %d", mental.getID());
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
     * @param asset
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

