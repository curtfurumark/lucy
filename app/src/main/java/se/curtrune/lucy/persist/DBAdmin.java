package se.curtrune.lucy.persist;

import static se.curtrune.lucy.util.Logger.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;

import se.curtrune.lucy.activities.economy.classes.Asset;
import se.curtrune.lucy.activities.economy.classes.Transaction;
import se.curtrune.lucy.activities.economy.persist.EcQueeries;
import se.curtrune.lucy.app.App;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.Type;

public class DBAdmin {

    public static boolean VERBOSE = false;
    public static Settings settings;

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
        item.setPeriod(cursor.getString(15));
        item.setEstimate(cursor.getString(16));
        item.setNotification(cursor.getString(17));
        item.setIsTemplate(cursor.getInt(18) != 0);
        //item.setMentalJson(cursor.getString(19));
        item.setContent(cursor.getString(19));
        item.setReward(cursor.getString(20));
        item.setColor(cursor.getInt(21));
        item.setPriority(cursor.getInt(22));
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
        if( item.getEstimate() != null){
            cv.put("estimate", item.getEstimate().toJson());
        }
        if( item.hasNotification()){
            cv.put("notification", item.getNotification().toJson());
        }
        cv.put("template", item.isTemplate()? 1:0);
        cv.put("color", item.getColor());
        cv.put("priority", item.getPriority());
        //cv.put("content", item.getContent());
        //cv.put("mental", item.getMental().toJson());
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

    /**
     * once upon a time i thought this was a good item, but i changed my mind
     * but i might change my mind again, it has been known to happen
     * @param cursor
     * @return MentalType a mental object, from the mental field in items, stored as json
     */
    public static Mental getMentalFromItem(Cursor cursor) {
        if( VERBOSE) log("DBAdmin.getMentalFromItem(Cursor)");
        return new Gson().fromJson(cursor.getString(0), Mental.class);
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
}


