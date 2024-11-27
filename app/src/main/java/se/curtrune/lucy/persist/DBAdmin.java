package se.curtrune.lucy.persist;

import static se.curtrune.lucy.util.Logger.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import se.curtrune.lucy.activities.economy.classes.Asset;
import se.curtrune.lucy.activities.economy.classes.Transaction;
import se.curtrune.lucy.activities.economy.persist.ECDBAdmin;
import se.curtrune.lucy.activities.economy.persist.EcQueeries;
import se.curtrune.lucy.app.App;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.classes.Type;

public class DBAdmin {

    public static boolean VERBOSE = false;
    public static Settings settings;
    public static void addColumnRepeatIdToTableItems(Context context){
        log("DBAdmin.alterColumnRepeatIdToTableItems(Context)");
        String queery = Queeries.addColumnRepeatIdToTableItems();
        try(LocalDB db = new LocalDB(context)){
            db.executeSQL(queery);
        }
    }



    /**
     * todo, add check if columns exists
     * @param context
     */
    private void addMentalToItemTable(Context context){
        log("...addMentalToItemTable()");
        String queeryEnergy = "ALTER TABLE items ADD COLUMN energy INTEGER DEFAULT 0";
        String queeryAnxiety = "ALTER TABLE items ADD COLUMN anxiety INTEGER DEFAULT 0";
        String queeryStress = "ALTER TABLE items ADD COLUMN stress INTEGER DEFAULT 0";
        String queeryMood = "ALTER TABLE items ADD COLUMN mood INTEGER DEFAULT 0";
        try(LocalDB db = new LocalDB(context)){
            db.executeSQL(queeryEnergy);
            db.executeSQL(queeryAnxiety);
            db.executeSQL(queeryStress);
            db.executeSQL(queeryMood);
            log(" energy column created?");
        }catch (Exception e){
            log("an exception occurred");
            e.printStackTrace();
        }
    }

    public static void createTables(Context context) {
        log("DBAdmin.createTables()");
        try(LocalDB  db = new LocalDB(context)) {
            db.executeSQL(Queeries.DROP_TABLE_MENTAL);
            db.executeSQL(Queeries.CREATE_TABLE_MENTAL);
            db.executeSQL(Queeries.DROP_TABLE_ITEMS);
            db.executeSQL(Queeries.CREATE_TABLE_ITEMS);
            db.executeSQL(EcQueeries.DROP_TABLE_TRANSACTIONS);
            db.executeSQL(EcQueeries.CREATE_TABLE_TRANSACTIONS);
            db.executeSQL(EcQueeries.DROP_TABLE_ASSETS);
            db.executeSQL(EcQueeries.CREATE_TABLE_ASSETS);
            log("...tables created");
        }
    }
    private void createLoggerTable(Context context){
        log("...createLoggerTable()");
        String queery = Queeries.CREATE_TABLE_LOGGER;
        try(LocalDB db = new LocalDB(context)){
            log("...queery", queery);
            db.executeSQL(queery);
        }
    }
    public static void createEconomyTables(Context context) {
        log("...createEconomyTables()");
        ECDBAdmin.createEconomyTables(context);
        //Toast.makeText(this, "tables created, possibly", Toast.LENGTH_LONG).show();
        DBAdmin.listTables(context);
    }

    public File getDataBaseFile(){
        log("...getDataBaseFile()");

        return null;
    }
    public static  void createItemsTable(Context context) {
        log("DBAdmin.createItemsTable()");
        try(LocalDB db = new LocalDB(context)) {
            db.executeSQL(Queeries.CREATE_TABLE_ITEMS);
        }
    }
    public static void createRepeatTable(Context context){
        log("...createRepeatTable");
        try(LocalDB db = new LocalDB(context)){
            db.executeSQL(Queeries.CREATE_TABLE_REPEAT);

        }
    }
    public static void dropTableItems(Context context) {
        try(LocalDB db = new LocalDB(context)) {
            db.executeSQL(Queeries.DROP_TABLE_ITEMS);
        }
    }
    public static  void dropTableMental(Context context) {
        log("DBAdmin.dropTableMental(Context)");
        try(LocalDB db = new LocalDB(context)){
            db.executeSQL(Queeries.DROP_TABLE_MENTAL);
        }
    }
    public static void dropTableRepeat(Context context){
        log("...dropTableRepeat(Context)");
        try(LocalDB db = new LocalDB(context)){
            db.executeSQL(Queeries.DROP_TABLE_REPEAT);
        }
    }

    public static void dropTables(Context context){
        log("DBAdmin.dropTables()");
        try(LocalDB db = new LocalDB(context)) {
            db.executeSQL(Queeries.DROP_TABLE_ITEMS);
            db.executeSQL(Queeries.DROP_TABLE_MENTAL);
            db.executeSQL(EcQueeries.DROP_TABLE_ASSETS);
            db.executeSQL(EcQueeries.DROP_TABLE_TRANSACTIONS);
        }
        log("...tables dropped");
    }

    public static String getCategory(Cursor cursor) {
        return cursor.getString(    0);
    }
    public static Item getItem(Cursor cursor){
        Item item = new Item();
        Gson gson = new Gson();
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
        String jsonRepeat = cursor.getString(15);
        if( jsonRepeat != null){
            Repeat repeat = gson.fromJson(jsonRepeat, Repeat.class);
            item.setRepeat(repeat);
        }
        Notification notification = gson.fromJson(cursor.getString(17), Notification.class);
        item.setNotification(notification);
        item.setIsTemplate(cursor.getInt(18) != 0);
        //item.setMentalJson(cursor.getString(19));
        item.setContent(cursor.getString(19));
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
        Gson gson = new Gson();
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
        if( item.getPeriod() != null) {
            cv.put("repeat", item.getPeriod().toJson());
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
        if( item.getType().equals(Type.MEDIA)){
            //cv.put("content",gson.toJson(item.getMedia()) );
        }
        //cv.put("content", item.getContent());
        cv.put("repeat_id", item.getRepeatID());
        return cv;
    }
    public static ContentValues getContentValues(Mental mental){
        if(VERBOSE)log("DBAdmin.getContentValues(MentalType)");
        ContentValues cv = new ContentValues();
        cv.put("itemID", mental.getItemID());
        cv.put("heading", mental.getHeading());
        cv.put("comment", mental.getComment());
        cv.put("energy", mental.getEnergy());
        cv.put("mood", mental.getMood());
        cv.put("stress", mental.getStress());
        cv.put("anxiety", mental.getAnxiety());
        cv.put("category", mental.getCategory());
        cv.put("date", mental.getDateEpoch());
        cv.put("updated", mental.getUpdatedEpoch());
        cv.put("created", mental.getCreatedEpoch());
        cv.put("time", mental.getTimeSecondOfDay());
        cv.put("isTemplate", mental.isTemplate());
        cv.put("isDone", mental.isDone() ? 1:0);
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

    public static Mental getMental(Cursor cursor) {
        if( VERBOSE) log("DBAdmin.getMental(Cursor)");
        Mental mental = new Mental();
        mental.setID(cursor.getLong(0));
        mental.setItemID(cursor.getLong(1));
        mental.setHeading(cursor.getString(2));
        mental.setComment(cursor.getString(3));
        mental.setCategory(cursor.getString(4));
        mental.setDate(cursor.getLong(5));
        mental.setTime(cursor.getInt(6));
        mental.setEnergy(cursor.getInt(7));
        mental.setMood(cursor.getInt(8));
        mental.setAnxiety(cursor.getInt(9));
        mental.setStress(cursor.getInt(10));
        mental.setCreated(cursor.getLong(11));
        mental.setUpdated(cursor.getLong(12));
        mental.setIsTemplate(cursor.getInt(13) == 1);
        mental.isDone(cursor.getInt(14) ==1);
        return mental;
    }
    public static Repeat getRepeat(Cursor cursor) {
        log("...getRepeat(Cursor)");
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
        try(LocalDB db = new LocalDB(context)) {
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
        try(LocalDB db = new LocalDB(context)) {
            db.getTableNames().forEach(System.out::println);
        }
    }
    public static List<String> getTableNames(Context context){
        log("DBAdmin.listTables()");
        try(LocalDB db = new LocalDB(context)) {
            return db.getTableNames();
        }
    }

    public static void addMentalColumnsToItemsTable(Context context) {
        log("...addMentalColumnsToItemsTable()");
        String queeryEnergy = "ALTER TABLE items ADD COLUMN energy INTEGER DEFAULT 0";
        String queeryAnxiety = "ALTER TABLE items ADD COLUMN anxiety INTEGER DEFAULT 0";
        String queeryStress = "ALTER TABLE items ADD COLUMN stress INTEGER DEFAULT 0";
        String queeryMood = "ALTER TABLE items ADD COLUMN mood INTEGER DEFAULT 0";
        try(LocalDB db = new LocalDB(context)){
            db.executeSQL(queeryEnergy);
            log("column energy added");
            db.executeSQL(queeryAnxiety);
            log("column anxiety added");
            db.executeSQL(queeryStress);
            log("column stress added");
            db.executeSQL(queeryMood);
            log("column mood created?");
        }catch (Exception e){
            log("an exception occurred");
            e.printStackTrace();
        }
    }
}


