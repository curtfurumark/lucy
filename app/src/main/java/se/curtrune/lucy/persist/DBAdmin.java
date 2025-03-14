package se.curtrune.lucy.persist;

import static se.curtrune.lucy.util.Logger.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import se.curtrune.lucy.activities.economy.classes.Asset;
import se.curtrune.lucy.activities.economy.classes.Transaction;
import se.curtrune.lucy.activities.economy.persist.ECDBAdmin;
import se.curtrune.lucy.activities.economy.persist.EcQueeries;
import se.curtrune.lucy.app.App;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.item.Item;
import se.curtrune.lucy.classes.ItemDuration;
import se.curtrune.lucy.classes.MedicineContent;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.util.gson.MyGson;

public class DBAdmin {

    public static boolean VERBOSE = false;
    public static Settings settings;

    @Deprecated
    public static void addColumnRepeatIdToTableItems(Context context){
        log("DBAdmin.alterColumnRepeatIdToTableItems(Context)");
        String queery = Queeries.ADD_COLUMN_REPEAT_ID_TO_ITEMS;
        try(SqliteLocalDB db = new SqliteLocalDB(context)){
            db.executeSQL(queery);
        }
    }
    public static void createTables(Context context) {
        log("DBAdmin.createTables()");
        try(SqliteLocalDB db = new SqliteLocalDB(context)) {
            db.executeSQL(Queeries.DROP_TABLE_ITEMS);
            db.executeSQL(Queeries.CREATE_TABLE_ITEMS);
            db.executeSQL(EcQueeries.DROP_TABLE_TRANSACTIONS);
            db.executeSQL(EcQueeries.CREATE_TABLE_TRANSACTIONS);
            db.executeSQL(EcQueeries.DROP_TABLE_ASSETS);
            db.executeSQL(EcQueeries.CREATE_TABLE_ASSETS);
            db.executeSQL(Queeries.DROP_TABLE_REPEAT);
            db.executeSQL(Queeries.CREATE_TABLE_REPEAT);
            log("...tables created");
        }
    }
    public static void createEconomyTables(Context context) {
        log(".DBAdmin.createEconomyTables(Context)");
        ECDBAdmin.createEconomyTables(context);
        DBAdmin.listTables(context);
    }

    public File getDataBaseFile(){
        log("...getDataBaseFile()");

        return null;
    }
    public static  void createItemsTable(Context context) {
        log("DBAdmin.createItemsTable()");
        try(SqliteLocalDB db = new SqliteLocalDB(context)) {
            db.executeSQL(Queeries.CREATE_TABLE_ITEMS);
        }
    }
    public static void createRepeatTable(Context context){
        log("...createRepeatTable");
        try(SqliteLocalDB db = new SqliteLocalDB(context)){
            db.executeSQL(Queeries.CREATE_TABLE_REPEAT);

        }
    }
    public static void dropTableItems(Context context) {
        try(SqliteLocalDB db = new SqliteLocalDB(context)) {
            db.executeSQL(Queeries.DROP_TABLE_ITEMS);
        }
    }
    public static  void dropTableMental(Context context) {
        log("DBAdmin.dropTableMental(Context)");
        try(SqliteLocalDB db = new SqliteLocalDB(context)){
            db.executeSQL(Queeries.DROP_TABLE_MENTAL);
        }
    }
    public static void dropTableRepeat(Context context){
        log("...dropTableRepeat(Context)");
        try(SqliteLocalDB db = new SqliteLocalDB(context)){
            db.executeSQL(Queeries.DROP_TABLE_REPEAT);
        }
    }

    public static void dropTables(Context context){
        log("DBAdmin.dropTables()");
        try(SqliteLocalDB db = new SqliteLocalDB(context)) {
            db.executeSQL(Queeries.DROP_TABLE_ITEMS);
            db.executeSQL(Queeries.DROP_TABLE_MENTAL);
            db.executeSQL(Queeries.DROP_TABLE_REPEAT);
            db.executeSQL(EcQueeries.DROP_TABLE_ASSETS);
            db.executeSQL(EcQueeries.DROP_TABLE_TRANSACTIONS);
        }
        log("...tables dropped");
    }
    public static void listColumns(String table, Context context){
        log("RepeatTest.listColumns(String table)");
        try(SqliteLocalDB db = new SqliteLocalDB(context)){
            db.getColumns(table);
        }
    }
    public static Item getItem(Cursor cursor){
        Item item = new Item();
        //Gson gson = new Gson();
        Gson gson = MyGson.INSTANCE.getMyGson();
        item.setId(cursor.getLong(0));
        item.setHeading(cursor.getString(1));
        item.setComment(cursor.getString(2));
        item.setTags(cursor.getString(3));
        item.setCreated(cursor.getLong(4));
        item.setUpdated(cursor.getLong(5));
        item.setTargetDate(cursor.getLong(6));
        item.setTargetTime(cursor.getInt(7));
        item.setCategory(cursor.getString(8));
        item.setType(cursor.getInt(9));
        item.setState(cursor.getInt(10));
        item.setHasChild(cursor.getInt(11) == 1);
        item.setDuration(cursor.getLong(12));
        item.setParentId(cursor.getInt(13));
        item.setIsCalenderItem(cursor.getInt(14) == 1);
/*        String jsonRepeat = cursor.getString(15);
        if( jsonRepeat != null){
            Repeat repeat = gson.fromJson(jsonRepeat, Repeat.class);
            item.setRepeat(repeat);
        }*/
        if( cursor.getString(16) != null) {
            String json = cursor.getString(16);
            log("item duration json  ", json);
            try{
                ItemDuration itemDuration = gson.fromJson(json, ItemDuration.class);
                item.setItemDuration(itemDuration);
            }catch (Exception e){
                log("exception", e.getMessage());
                e.printStackTrace();
            }

            //item.setDurationType();
        }
        Notification notification = gson.fromJson(cursor.getString(17), Notification.class);
        item.setNotification(notification);
        item.setIsTemplate(cursor.getInt(18) != 0);
        String jsonContent = cursor.getString(19);
        if( jsonContent != null && !jsonContent.isEmpty()){
            int ordinal = cursor.getInt(9);
            if( ordinal == Type.MEDICIN.ordinal()){
                MedicineContent content = gson.fromJson(jsonContent, MedicineContent.class);
                item.setContent(content);
            }
        }
        //item.setReward(cursor.getString(20));
        item.setColor(cursor.getInt(21));
        item.setPriority(cursor.getInt(22));
        item.setEnergy(cursor.getInt(23));
        item.setAnxiety(cursor.getInt(24));
        item.setStress(cursor.getInt(25));
        item.setMood(cursor.getInt(26));
        item.setRepeatID(cursor.getInt(27));
        return item;
    }
    public static ContentValues getContentValues(Asset asset){
        ContentValues cv = new ContentValues();
        cv.put("account", asset.getAccount());
        cv.put("amount", asset.getAmount());
        cv.put("date", asset.getDate().toEpochDay());
        return cv;
    }

    public static ContentValues getContentValues(Item item) {
        if( VERBOSE) log("DBAdmin.getContentValues(Item)");
        Gson gson = MyGson.INSTANCE.getMyGson();
        ContentValues cv = new ContentValues();
        cv.put("heading", item.getHeading());
        cv.put("comment", item.getComment());
        cv.put("tags", item.getTags());
        cv.put("created", item.getCreatedEpoch());
        cv.put("updated", item.getUpdatedEpoch());
        cv.put("targetDate", item.getTargetDateEpochDay());
        cv.put("targetTime", item.getTargetTimeSecondOfDay());
        cv.put("type", item.getType().ordinal());
        cv.put("state", item.getState().ordinal());
        cv.put("hasChild", item.hasChild() ? 1: 0);
        cv.put("duration", item.getDuration());
        cv.put("isCalenderItem", item.isCalenderItem()? 1:0);
        cv.put("category", item.getCategory());
        cv.put("parentID", item.getParentId());
        if( item.getRepeat() != null) {
            cv.put("repeat", item.getRepeat().toJson());
        }
        //well change name of field, please TODO
        if( item.getItemDuration() != null){
            log("item has duration, adding to field estimate");
            cv.put("estimate", gson.toJson(item.getItemDuration()));
        }
/*        if( item.getEstimate() != null){
            cv.put("estimate", item.getEstimate().toJson());
        }*/
        if( item.hasNotification()){
            cv.put("notification", item.getNotification().toJson());
        }
        cv.put("template", item.isTemplate()? 1:0);
        cv.put("color", item.getColor());
        cv.put("priority", item.getPriority());
        cv.put("energy", item.getEnergy());
        cv.put("anxiety", item.getAnxiety());
        cv.put("stress", item.getStress());
        cv.put("mood", item.getMood());
        if( item.getContent() != null){
            cv.put("content", gson.toJson(item.getContent()));
        }
/*        if( item.getType().equals(Type.MEDIA)){
            cv.put("content",gson.toJson(item.getContent()) );
        }*/
        if( item.getID() > 0){ // for restoring deleted item,to ensure they don't get a new id
            log("inserting deleted item, trying to restore");
            cv.put("id", item.getID());
        }
        cv.put("repeat_id", item.getRepeatID());
        return cv;
    }


    public static ContentValues getContentValues(Repeat repeat) {
        log("DBAdmin.getContentValues(Repeat)");
        ContentValues cv = new ContentValues();
        Gson gson = new Gson();
        cv.put("json",gson.toJson(repeat) );
        return cv;
    }
    public static ContentValues getContentValues(Transaction transaction){
        ContentValues cv = new ContentValues();
        cv.put("description", transaction.getDescription());
        cv.put("amount", transaction.getAmount());
        cv.put("date", transaction.getDate().toEpochDay());
        return cv;
    }
    public static Repeat getRepeat(Cursor cursor) {
        if( VERBOSE)log("...getRepeat(Cursor)");
        long id = cursor.getLong(0);
        String json = cursor.getString(1);
        Repeat repeat =  new Gson().fromJson(json, Repeat.class);
        repeat.setID(id);
        return repeat;
    }



    /**
     * creates default lists/items
     * these being the ROOT of everything
     * saves root ids in sharedPrefs...
     * children to root: today, todo, projects, appointments, panic
     * @param context, context context context
     */
    public static void insertRootItems(Context context) {
        log("...insertRootItems(Context)");
        Settings settings = Settings.getInstance(context);
        try(SqliteLocalDB db = new SqliteLocalDB(context)) {
            //create the root item
            Item root = new Item("root");
            root.setType(Type.ROOT);
            root = db.insert(root);
            settings.addRootID(Settings.Root.THE_ROOT, root.getID(), context);

            Item todayRoot = App.getRootItem("today");
            Item todoRoot = App.getRootItem("todo");
            Item projectsRoot = App.getRootItem("projects");
            Item appointmentsRoot = App.getRootItem("appointments");
            Item panicRoot = App.getRootItem("panicList");
            //Item panicAttacks = App.getRootItem("panicAttacks");

            appointmentsRoot = db.insertChild(root, appointmentsRoot);
            settings.addRootID(Settings.Root.APPOINTMENTS, appointmentsRoot.getID(), context);
            todayRoot = db.insertChild(root, todayRoot);
            settings.addRootID(Settings.Root.DAILY, todayRoot.getID(), context);
            projectsRoot = db.insertChild(root, projectsRoot);
            settings.addRootID(Settings.Root.PROJECTS, projectsRoot.getID(), context);
            todoRoot = db.insertChild(root, todoRoot);
            settings.addRootID(Settings.Root.TODO, todoRoot.getID(), context);
            panicRoot = db.insertChild(root, panicRoot);
            settings.addRootID(Settings.Root.PANIC, panicRoot.getID(), context);
        }
    }

    public static void listTables(Context context) {
        log("DBAdmin.listTables()");
        try(SqliteLocalDB db = new SqliteLocalDB(context)) {
            db.getTableNames().forEach(System.out::println);
        }
    }
    public static List<String> getTableNames(Context context){
        log("DBAdmin.listTables()");
        try(SqliteLocalDB db = new SqliteLocalDB(context)) {
            return db.getTableNames();
        }
    }

    public static void addMentalColumnsToItemsTable(Context context) {
        log("DBAdmin.addMentalColumnsToItemsTable(Context)");
        try(SqliteLocalDB db = new SqliteLocalDB(context)){
            db.executeSQL(Queeries.ADD_COLUMN_ANXIETY_TO_ITEMS);
            log("column anxiety added");
            db.executeSQL(Queeries.ADD_COLUMN_ENERGY_TO_ITEMS);
            log("column energy added");
            db.executeSQL(Queeries.ADD_COLUMN_MOOD_TO_ITEMS);
            log("column mood added");
            db.executeSQL(Queeries.ADD_COLUMN_STRESS_TO_ITEMS);
            log("column stress created?");
        }catch (Exception e){
            log("an exception occurred");
            e.printStackTrace();
        }
    }
}


