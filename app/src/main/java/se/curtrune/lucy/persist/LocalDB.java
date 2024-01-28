package se.curtrune.lucy.persist;

import static se.curtrune.lucy.util.Logger.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.util.Settings;

public class LocalDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "lucy.db";
    private static final String ITEMS_TABLE = "items";
    private static final String TABLE_MENTAL = "mental";
    private static final String TABLE_CATEGORIES = "categories";
    private static final int DB_VERSION = 1;
    public static boolean VERBOSE = true;

    private SQLiteDatabase db;

    public LocalDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        log("LocalDB.onCreate(SQLiteDatabase)");
        sqLiteDatabase.execSQL(Queeries.CREATE_TABLE_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void close(){
        log("LocalDB.close()");
        if( db != null && db.isOpen()){
            db.close();
            db = null;
        }
    }

    public void createTables(SQLiteDatabase database){
        log("LocalDB.createTables()");
        database.execSQL(Queeries.CREATE_TABLE_MENTAL);
        database.execSQL(Queeries.CREATE_TABLE_CATEGORIES);
        database.execSQL(Queeries.CREATE_TABLE_ITEMS);
    }
    public int delete(Item item) {
        log("LocalDB.delete(Item)");
        db = this.getWritableDatabase();
        String whereClause = String.format("id = %d", item.getID());
        int rowsAffected = db.delete(ITEMS_TABLE, whereClause, null);
        if( rowsAffected != 1){
            log("some kind of error deleting item");
        }
        db.close();
        return rowsAffected;
    }
    public void executeSQL(String sql){
        log("LocalDB.executeSQL(String sql)", sql);
        db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
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

    public Item insert(Item item) throws SQLException {
        log("LocalDB.insert(Item)");
        if( VERBOSE) log(item);
        db = this.getWritableDatabase();
        long id = db.insertOrThrow(ITEMS_TABLE, null,DBAdmin.getContentValues(item));
        if( id == -1 ){
            log("...return id -1, not good enough");
        }else{
            log("...item inserted with id ", id);
        }
        item.setId((int) id);
        db.close();
        return item;
    }

    public Mental insert(Mental mental) throws SQLException {
        log("LocalDB.insert(Mental)");
        db = this.getWritableDatabase();
        long id = db.insertOrThrow(TABLE_MENTAL, null, DBAdmin.getContentValues(mental));
        db.close();
        mental.setID(id);
        return mental;
    }
    public Item insertChild(Item parent, Item child){
        log("LocalDB.insertChild(Item, Item)");
        child.setParentId(parent.getID());
        db = this.getWritableDatabase();
        long res = db.insert(ITEMS_TABLE, null, DBAdmin.getContentValues(child));
        if( res == -1){
            log("...errror inserting child");
            db.close();
            return null;
        }else{
            child.setId(res);
        }
        if( !parent.hasChild()){
            parent.setHasChild(true);
            String whereClause = String.format(Locale.ENGLISH, "id = %d", parent.getID());
            int rowsAffected = db.update(ITEMS_TABLE,DBAdmin.getContentValues(parent), whereClause, null);
            if( rowsAffected != 1){
                log("...errror updating parent has child");
            }
        }
        db.close();
        return child;

    }
    public void insertCategory(String category){
        db = this.getWritableDatabase();
        //db.insert(TABLE_CATEGORIES, null, DBAdmin.getContentValues())

    }
    public void open(){
        if( VERBOSE) log("LocalDB.open()");
        db = this.getReadableDatabase();
    }
    public void populateDatabase(){
        log("LocalDB.populateDatabase()");
        for(String category: Settings.CATEGORIES){
            insertCategory(category);
        }

    }
    public void resetDatabase(){
        log("LocalDB.resetDatabase()");
        db = this.getWritableDatabase();
        db.execSQL(Queeries.DELETE_ITEMS_TABLE);
        db.execSQL(Queeries.DROP_TABLE_CATEGORIES);
        db.execSQL(Queeries.DROP_TABLE_MENTAL);
        //for(String stiv)
    }


    /**
     *
     * @param id, of the item which to set field hasChild
     * @return true if operation succeeded
     */
    public boolean setItemHasChild(long id, boolean hasChild) {
        log("...setItemHasChild(long id)", id);
        db = this.getWritableDatabase();
        String whereClause = String.format(Locale.ENGLISH, "id = %d", id);
        ContentValues cv = new ContentValues();
        cv.put("hasChild", hasChild ? 1:0);
        int rowsAffected = db.update(ITEMS_TABLE, cv,  whereClause, null);
        if( rowsAffected != 1 ){
            log("ERROR setItemHasChild...");
        }
        db.close();
        return rowsAffected == 1;
    }

    public List<Item > selectItems(LocalDate date, State state) {
        log("LocalDB.selectItems(LocalDate, State)", date.toString() + ", " + state.toString());
        String queery = Queeries.selectItems(date, state);
        return selectItems(queery);
    }
    public List<Item> selectItems(LocalDate date){
        return selectItems(Queeries.selectItems(date));
    }
    public List<Item> selectItems(String query){
        log("LocalDB.selectItems(String query)", query);
        db = this.getReadableDatabase();
        List<Item> items = new ArrayList<>();
        Cursor cursor = db.rawQuery(query,null);
        if( cursor.moveToFirst()){
            do{
                items.add(DBAdmin.getItem(cursor));

            }while(cursor.moveToNext());
        }
        db.close();
        return items;
    }
    public List<Item> selectChildren(Item parent){
        log("LocalDB.selectChildren(Item)" , parent != null? parent.getHeading():"parent id: 0" );
        String queery = Queeries.selectChildren(parent);
        return selectItems(queery);

    }

    public List<Mental> selectMental() {
        log("LocalDB.selectMental()");
        String query = Queeries.selectMental();
        List<Mental> items = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if( cursor.moveToFirst()){
            do {
                items.add(DBAdmin.getMental(cursor));
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return items;
    }

    public List<Item> selectToday() {
        log("LocalDB.selectToday()");
        db = this.getReadableDatabase();
        List<Item> items = new ArrayList<>();
        Cursor cursor = db.rawQuery(Queeries.selectItems(),null);
        if( cursor.moveToFirst()){
            do{
                items.add(DBAdmin.getItem(cursor));

            }while(cursor.moveToNext());
        }
        db.close();
        return items;
    }

    public void touchParents(Item item, Context context){
        log("LocalDB.touchParents(Item)");
        //select * from items where parent_id = id OR id = id;
        //updated

    }

    public int update(Item item) {
        log("LocalDB.update(Item item)");
        db = this.getWritableDatabase();
        String whereClause = String.format("id = %d", item.getID());
        int rowsAffected = db.update(ITEMS_TABLE, DBAdmin.getContentValues(item),whereClause, null );
        log("...update ok: ", rowsAffected == 1);
        db.close();
        return rowsAffected;
    }

    public int update(Mental mental) {
        log("LocalDB.update(Mental)");
        log(mental);
        db = this.getWritableDatabase();
        String whereClause = String.format("id = %d", mental.getID());
        int rowsAffected = db.update(TABLE_MENTAL,DBAdmin.getContentValues(mental), whereClause, null);
        log("....rowsAffected", rowsAffected);
        db.close();
        return rowsAffected;
    }


    public String[] getCategories() {
        log("LocalDB.getCategories()");
        db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(Queeries.selectCategories(), null);
        String[] categoriesArray = new String[cursor.getCount()];
        int index = 0;
        if( cursor.moveToFirst()){
            do {
                String category = DBAdmin.getCategory(cursor);
                categoriesArray[index++] = category;
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return categoriesArray;
    }

    public List<Item> selectItems(LocalDate date, Context context) {
        log("LocalDB.selectItems(LocalDate) ", date.toString());
        LocalDB db = new LocalDB(context);
        return db.selectItems(Queeries.selectItems(date));
    }
    public List<Item> selectItems(Type type){
        log("LocalDB.selectItems(Type)");
        return selectItems(Queeries.selectItems(type));

    }

    public void touch(Item item) {
        log("LocalDB.touch(Item)");
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("updated", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        String whereClause = String.format("id = %d", item.getID());
        int rowsAffected = db.update(ITEMS_TABLE,cv, whereClause, null  );
        log("...rowsAffected", rowsAffected);

    }

    public List<Mental> selectMentals(String query) {
        log("...selectMentals(String)", query);
        List<Mental> items = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if( cursor.moveToFirst()){
            do {
                items.add(DBAdmin.getMental(cursor));
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return items;

    }

    /**
     *
     * @param query the query to be executed
     * @return Mental if there is such a thing or null if not found
     */
    public Mental selectMental(String query) {
        log("LocalDB.selectMental(String)", query);
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Mental mental = null;
        if( cursor.moveToFirst()){
            mental = DBAdmin.getMental(cursor);
        }
        db.close();
        cursor.close();
        return mental;
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
        if( cursor.moveToFirst()){
            item = DBAdmin.getItem(cursor);
        }else{
            log("...error, no item with id", id);
        }
        cursor.close();
        db.close();
        return item;
    }

    public List<Mental> selectMentals(LocalDate date) {
        log("...selectMentals(LocalDate)", date.toString());
        String query = Queeries.selectMentals(date);
        return selectMentals(query);
    }


}
