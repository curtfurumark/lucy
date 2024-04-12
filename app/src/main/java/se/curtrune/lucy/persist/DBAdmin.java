package se.curtrune.lucy.persist;

import static se.curtrune.lucy.util.Logger.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.app.Settings;

public class DBAdmin {

    public static boolean VERBOSE = false;
    public static Settings settings;

    public static void createTables(Context context) {
        log("DBAdmin.createTables()");
        LocalDB  db = new LocalDB(context);
        db.executeSQL(Queeries.CREATE_TABLE_MENTAL);
        db.executeSQL(Queeries.CREATE_TABLE_CATEGORIES);
        db.executeSQL(Queeries.CREATE_TABLE_ITEMS);
        log("...tables created");
    }
    public static void dropTables(Context context){
        log("DBAdmin.dropTables()");
        LocalDB db = new LocalDB(context);
        db.executeSQL(Queeries.DROP_TABLE_CATEGORIES);
        db.executeSQL(Queeries.DROP_TABLE_ITEMS);
        db.executeSQL(Queeries.DROP_TABLE_MENTAL);

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
        item.setHasChild(cursor.getInt(11) == 1 ? true: false);
        item.setDuration(cursor.getLong(12));
        item.setParentId(cursor.getInt(13));
        //item.setDays(cursor.getInt(14));
        item.setPeriod(cursor.getString(15));
        item.setEstimate(cursor.getString(16));
        String json = cursor.getString(17);
        log("...json", json);
        item.setNotification(cursor.getString(17));
        item.setIsTemplate(cursor.getInt(18) == 0? false: true);
        return item;
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
        //cv.put("days", item.getDays());
        cv.put("category", item.getCategory());
        cv.put("parentID", item.getParentId());
        if( item.getPeriod() != null) {
            cv.put("period", item.getPeriod().toJson());
        }
        if( item.getEstimate() != null){
            cv.put("estimate", item.getEstimate().toJson());
        }
        if( item.hasNotification()){
            cv.put("notification", item.getNotification().toJson());
        }
        cv.put("template", item.isTemplate()? 1:0);
        return cv;
    }
    public static ContentValues getContentValues(Mental mental){
        if(VERBOSE)log("DBAdmin.getContentValues(Mental)");
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
        cv.put("time", mental.getTimeSecondOfDay());
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
        return mental;
    }

    public static ContentValues getContentValues(String category) {
        log("DBAdmin.getContentValues(String category) ", category);
        ContentValues cv = new ContentValues();
        cv.put("name", category);
        return cv;
    }



    public static void insertCategories(Context context) {
        log("...insertCategories(Context)");
        LocalDB db = new LocalDB(context);
        for( String category: Settings.getCategories()){
            db.insertCategory(category);
        }
    }

    public static void insertRootItems(Context context) throws SQLException {
        log("...insertRootItems(Context)");
        Settings settings = Settings.getInstance(context);
        LocalDB db = new LocalDB(context);
        Item todayRoot = Settings.getTodayRoot();
        Item todoRoot = Settings.getTodoRoot();
        Item projectsRoot = Settings.getProjectsRoot();
        Item appointmentsRoot = Settings.getAppointmentsRoot();
        appointmentsRoot = db.insert(appointmentsRoot);
        settings.addRootID(Settings.Root.APPOINTMENTS, appointmentsRoot.getID(), context);
        todayRoot = db.insert(todayRoot);
        settings.addRootID(Settings.Root.DAILY, todayRoot.getID(), context);
        projectsRoot = db.insert(projectsRoot);
        settings.addRootID(Settings.Root.PROJECTS, projectsRoot.getID(), context);
        todoRoot = db.insert(todoRoot);
        settings.addRootID(Settings.Root.TODO, todoRoot.getID(), context);
    }

    private static Item getRemember(String heading, LocalDate date, LocalTime time){
        Item item = new Item(heading);
        return item;
    }

}
