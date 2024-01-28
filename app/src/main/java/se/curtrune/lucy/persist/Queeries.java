package se.curtrune.lucy.persist;

import java.time.LocalDate;
import java.util.Locale;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;

public class Queeries {
    public static final String DELETE_ITEMS_TABLE = "DROP TABLE items";
    public static final String DROP_TABLE_CATEGORIES = "DROP TABLE categories";
    public static final  String DROP_TABLE_MENTAL = "DROP table mental";
    public static String CREATE_TABLE_ITEMS =
            "CREATE TABLE items  " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "heading TEXT, " +
                    "comment TEXT, " +
                    "tags TEXT, " +
                    "created INTEGER, " +
                    "updated INTEGER, " +
                    "targetDate INTEGER, " +
                    "targetTime INTEGER, " +
                    "category INTEGER, " +
                    "type INTEGER, " +
                    "state INTEGER, " +
                    "hasChild INTEGER, " +
                    "duration INTEGER, " +
                    "parentID INTEGER," +
                    "days INTEGER )";
    public static String CREATE_TABLE_MENTAL =
        "CREATE TABLE mental (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "itemID INTEGER, " +
                "heading TEXT, " +
                "comment TEXT, " +
                "category TEXT,"  +
                "date INTEGER, " +
                "time  INTEGER, " +
                "energy INTEGER, " +
                "mood INTEGER, " +
                "anxiety INTEGER, " +
                "stress INTEGER, " +
                "created INTEGER, " +
                "updated INTEGER)";
    public static String CREATE_TABLE_CATEGORIES = "CREATE TABLE categories " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";

    public static String selectItems(){
        return "SELECT * FROM items ORDER BY updated DESC";
    }
    public static String selectItems(LocalDate targetDate){
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE targetDate = %d ORDER BY updated", targetDate.toEpochDay());
    }
    public static String selectItems(LocalDate targetDate, State state){
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE targetDate = %d AND state = %d", targetDate.toEpochDay(), state.ordinal());
    }
    public static String selectCategories() {
        return "SELECT name FROM categories ORDER BY name";
    }

    /**
     * selects direct items to given parent
     * @param parent, the parent, if is null, selects root items
     * @return a list of children , or an empty list if none found
     */
    public static String selectChildren(Item parent) {
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE parentID = %d ORDER by updated DESC", parent != null? parent.getID(): 0);
    }


    public static String selectMental() {
        return "SELECT * FROM mental ORDER  BY created DESC";
    }

    public static String selectItems(State state) {
        return String.format("SELECT * FROM items WHERE state = %d ORDER BY targetDate DESC", state.ordinal());
    }
    public static String selectItems(State state, LocalDate date) {
        return String.format("SELECT * FROM items WHERE state = %d  AND date = %d ORDER BY targetDate DESC",
                state.ordinal(),
                date.toEpochDay());
    }

    public static String insertCategory(String category) {
        return String.format("INSERT INTO categories (name) values ('%s')", category);
    }

    public static String selectTodayList(LocalDate date) {
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE (state = %d AND targetDate <= %d)  OR " +
                "(state = %d AND targetDate = %d)", State.INFINITE.ordinal(), date.toEpochDay(), State.DONE.ordinal(), date.toEpochDay());
    }

    public static String selectItems(Type type) {
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE type = %d ORDER BY updated", type.ordinal());
    }

    public static String selectMentals(LocalDate date) {
        return String.format("SELECT * FROM mental WHERE date = %d", date.toEpochDay());
    }

    public static String selectLatestMentals(int limit) {
        return String.format("SELECT * FROM mental ORDER BY updated LIMIT 10");
    }

    public static String selectItem(long id) {
        return String.format("SELECT  *  FROM items WHERE id = %d", id);
    }
}
