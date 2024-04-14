package se.curtrune.lucy.persist;

import static se.curtrune.lucy.util.Logger.log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Locale;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.fragments.TopTenFragment;

public class Queeries {
    public static final String DROP_TABLE_ITEMS = "DROP TABLE items";
    public static final String DROP_TABLE_CATEGORIES = "DROP TABLE categories";
    public static final  String DROP_TABLE_MENTAL = "DROP table mental";
    //TODO
    public static final String CREATE_TABLE_ECONOMY = "CREATE TABLE economy " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "description TEXT, " +
            "amount INTEGER )";
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
                    "days INTEGER, " +
                    "period STRING, " +
                    "estimate STRING," +
                    "notification STRING," +
                    "template INTEGER default 0)";
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

    public static boolean VERBOSE = true;
    public static String insertCategory(String category) {
        return String.format("INSERT INTO categories (name) values ('%s')", category);
    }

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




    public static String selectItems(State state) {
        if( VERBOSE) log("Queeries.selectItems(State)", state.toString());
        return String.format("SELECT * FROM items WHERE state = %d  AND type != %d AND hasChild = 0 ORDER BY targetDate DESC", state.ordinal(), Type.ROOT.ordinal());
    }
    public static String selectItems(State state, LocalDate date) {
        return String.format("SELECT * FROM items WHERE state = %d  AND date = %d ORDER BY targetDate DESC",
                state.ordinal(),
                date.toEpochDay());
    }



    public static String selectTodayList(LocalDate date) {
        LocalDateTime.now().toLocalDate();
        LocalDateTime startLocalDateTime = date.atStartOfDay();
        long startEpoch = startLocalDateTime.toEpochSecond(ZoneOffset.UTC);
        long endEpoch = startEpoch + (3600 * 24);
        //AND (updated)
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE " +
                        "(template = %d AND targetDate <= %d)  OR " +      //INFINITE today or earlier
                        //"(state = %d AND targetDate = %d) OR " +        //items done today
                        "(targetDate = %d AND hasChild = 0 AND state = %d) OR " +   //items todo today
                        "(state = %d AND updated >= %d AND updated <= %d)", //items done today, but targetDate not today
                1, date.toEpochDay(),
                //State.DONE.ordinal(), date.toEpochDay(),
                date.toEpochDay(), State.TODO.ordinal(),
                State.DONE.ordinal(), startEpoch, endEpoch);
    }
    public static String selectTodayList2(LocalDate date) {
        LocalDateTime.now().toLocalDate();
        LocalDateTime startLocalDateTime = date.atStartOfDay();
        long startEpoch = startLocalDateTime.toEpochSecond(ZoneOffset.UTC);
        long endEpoch = startEpoch + (3600 * 24);
        //AND (updated)
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE " +
                        "(template = %d AND targetDate <= %d)  OR " +      //INFINITE today or earlier
                        "(state = %d AND targetDate = %d) OR " +        //items done today
                        "(targetDate = %d AND hasChild = 0 AND state = %d) OR " +   //items todo today
                        "(state = %d AND updated >= %d AND updated <= %d)", //items done today, but targetDate not today
                1, date.toEpochDay(),
                State.DONE.ordinal(), date.toEpochDay(),
                date.toEpochDay(), State.TODO.ordinal(),
                State.DONE.ordinal(), startEpoch, endEpoch);
    }

    public static String selectItems(Type type) {
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE type = %d ORDER BY updated", type.ordinal());
    }

    public static String selectMentals() {
        return "SELECT * FROM mental ORDER  BY created DESC";
    }



    public static String selectLatestMentals(int limit) {
        return String.format("SELECT * FROM mental ORDER BY updated LIMIT 10");
    }

    public static String selectItem(long id) {
        return String.format("SELECT  *  FROM items WHERE id = %d", id);
    }

    public static String selectItems(LocalDate firstDate, LocalDate lastDate) {
        String query = String.format(Locale.ENGLISH, "SELECT * FROM items WHERE ()");
        return null;
    }
    public static String selectItems(LocalDate firstDate, LocalDate lastDate, State state) {
        LocalDateTime localDateTimeFirst = firstDate.atStartOfDay();
        long startEpoch = localDateTimeFirst.toEpochSecond(ZoneOffset.UTC);
        long endEpoch = lastDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) + (3600 *24);
        //long endEpoch = startEpoch + (3600 * 24);
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE " +
                "(state = %d AND  updated >= %d AND updated <= %d) ORDER BY updated DESC",
                state.ordinal(),startEpoch, endEpoch);
    }

    public static String selectMental(Item item) {
        return String.format("SELECT * FROM mental WHERE itemID = %d", item.getID());
    }
    public static String selectMentals(LocalDate date) {
        return String.format("SELECT * FROM mental WHERE date = %d", date.toEpochDay());
    }

    public static String selectMentals(LocalDate firstDate, LocalDate lastDate) {
        return String.format("SELECT * FROM mental WHERE date >= %d AND date <= %d ORDER BY date DESC", firstDate.toEpochDay(), lastDate.toEpochDay());
    }

    public static String selectTopTen() {
        return "SELECT * FROM mental ORDER BY energy DESC LIMIT 10";
    }

    public static String selectMentalTopTen(TopTenFragment.Mode mode) {
        return String.format( "SELECT * FROM mental ORDER BY %s DESC LIMIT 10", mode.toString().toLowerCase());
    }
}
