package se.curtrune.lucy.persist;

import static se.curtrune.lucy.util.Logger.log;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;

public class DBAdmin {

    public static boolean VERBOSE = false;

    public static List<Item> getItems(Cursor cursor){
        List<Item> items = new ArrayList<>();
        if( cursor.moveToFirst()){
            do{
                items.add(getItem(cursor));
            }while(cursor.moveToNext());
        }
        return items;
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
        item.setType(cursor.getInt(9));
        item.setState(cursor.getInt(10));
        item.setHasChild(cursor.getInt(11) == 1 ? true: false);
        item.setDuration(cursor.getLong(12));
        item.setParentId(cursor.getInt(13));
        item.setDays(cursor.getInt(14));
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
        cv.put("days", item.getDays());
        cv.put("parentID", item.getParentId());
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
        log("DBAdmin.getMental(Cursor)");
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

    public static String getCategory(Cursor cursor) {
        return cursor.getString(0);
    }
}
