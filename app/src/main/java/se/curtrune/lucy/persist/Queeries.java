package se.curtrune.lucy.persist;

import static se.curtrune.lucy.util.Logger.log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Locale;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Message;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.fragments.TopTenFragment;

public class Queeries {
    public static final String DROP_TABLE_ITEMS = "DROP TABLE IF EXISTS items ";
    //public static final String DROP_TABLE_CATEGORIES = "DROP TABLE IF EXISTS categories";
    public static final String DROP_TABLE_LOGGER = "DROP TABLE IF EXISTS logger";
    public static final String DROP_TABLE_MENTAL = "DROP TABLE IF EXISTS mental";
    public static final String CREATE_TABLE_LOGGER = " CREATE TABLE logger " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "created INTEGER, " +
            "message TEXT)";
    //TODO

    public static String CREATE_TABLE_ITEMS =
            "CREATE TABLE items  " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "heading TEXT, " +          //1
                    "comment TEXT, " +          //2
                    "tags TEXT, " +             //3
                    "created INTEGER, " +       //4
                    "updated INTEGER, " +       //5
                    "targetDate INTEGER, " +    //6
                    "targetTime INTEGER, " +    //7
                    "category INTEGER, " +      //8
                    "type INTEGER, " +          //9
                    "state INTEGER, " +         //10
                    "hasChild INTEGER, " +      //11
                    "duration INTEGER, " +      //12
                    "parentID INTEGER," +       //13
                    "isCalenderItem INTEGER, " +          //14
                    "repeat STRING, " +         //15
                    "estimate STRING," +        //16
                    "notification STRING," +    //17
                    "template INTEGER default 0, " +  //18
                    //"mental STRING, " +         //19
                    "content STRING, " +         //19
                    "reward STRING, " +         //20
                    "color INTEGER default -1, " + //21
                    "priority INTEGER default 0" + //22
                    ")";           //20
    public static String CREATE_TABLE_MENTAL =
            "CREATE TABLE mental (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "itemID INTEGER, " +        //1
                    "heading TEXT, " +          //2
                    "comment TEXT, " +          //3
                    "category TEXT," +          //4
                    "date INTEGER, " +          //5
                    "time  INTEGER, " +         //6
                    "energy INTEGER, " +        //7
                    "mood INTEGER, " +          //8
                    "anxiety INTEGER, " +       //9
                    "stress INTEGER, " +        //10
                    "created INTEGER, " +       //11
                    "updated INTEGER, " +       //12
                    "isTemplate INTEGER, " +      //13
                    "isDone DEFAULT 0" +        //14
                    ")";
/*    public static String CREATE_TABLE_CATEGORIES = "CREATE TABLE categories " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";*/

    public static boolean VERBOSE = false;
    public static String insert(Message message) {
        return String.format(Locale.getDefault(), "INSERT INTO messages(subject, content, user, created) values " +
                "('%s', '%s', '%s', %d)", message.getSubject(), message.getContent(), message.getUser(), message.getCreatedEpoch());
    }

    public static String insertCategory(String category) {
        return String.format("INSERT INTO categories (name) values ('%s')", category);
    }

    public static String selectAppointments() {
        return String.format(Locale.getDefault(), "SELECT * FROM items WHERE type = %d ORDER BY targetDate DESC",
                Type.APPOINTMENT.ordinal());
    }
    public static String selectAppointments(LocalDate date) {
        return String.format(Locale.getDefault(), "SELECT * FROM items WHERE (type = %d  OR isCalenderItem = 1)  AND targetDate = %d ORDER BY targetDate DESC",
                Type.APPOINTMENT.ordinal(),date.toEpochDay());
    }
    public static String selectItems() {
        return "SELECT * FROM items ORDER BY updated DESC";
    }

    public static String selectItems(LocalDate targetDate) {
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE targetDate = %d ORDER BY updated", targetDate.toEpochDay());
    }

    public static String selectItems(LocalDate targetDate, State state) {
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE targetDate = %d AND state = %d", targetDate.toEpochDay(), state.ordinal());
    }
/*    public static String selectCategories() {
        return "SELECT name FROM categories ORDER BY name";
    }*/

    /**
     * selects direct items to given parent
     *
     * @param parent, the parent, if is null, selects root items
     * @return a list of children , or an empty list if none found
     */
    public static String selectChildren(Item parent) {
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE parentID = %d ORDER by updated DESC", parent != null ? parent.getID() : 0);
    }


    public static String selectItems(State state) {
        if (VERBOSE) log("Queeries.selectItems(State)", state.toString());
        return String.format(Locale.getDefault(), "SELECT * FROM items WHERE state = %d  AND type != %d AND hasChild = 0 ORDER BY targetDate DESC", state.ordinal(), Type.ROOT.ordinal());
    }

    public static String selectItems(State state, LocalDate date) {
        return String.format(Locale.getDefault(), "SELECT * FROM items WHERE state = %d  AND date = %d ORDER BY targetDate DESC",
                state.ordinal(),
                date.toEpochDay());
    }


    /**
     * this one is supposed to selectItem that are
     *      calenderItems with targetDate to specified date
     *      any items that are done today
     * @param date, the date you've shown an interest in
     * @return a appropriate valid sql queery
     */
    public static String selectTodayList(LocalDate date) {
        LocalDateTime.now().toLocalDate();
        LocalDateTime startLocalDateTime = date.atStartOfDay();
        long startEpoch = startLocalDateTime.toEpochSecond(ZoneOffset.UTC);
        long endEpoch = startEpoch + (3600 * 24);
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE " +
                        "(template = %d AND targetDate <= %d AND targetDate > 0)  OR " +      //INFINITE today or earlier
                        "(targetDate = %d AND hasChild = 0 AND state = %d AND isCalenderItem = 1) OR " +   //items todo today
                        "(state = %d AND updated >= %d AND updated <= %d)", //items done today, but targetDate not today
                1, date.toEpochDay(),
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
        return String.format(Locale.getDefault(), "SELECT * FROM mental ORDER BY updated LIMIT %d", limit);
    }

    public static String selectItem(long id) {
        return String.format(Locale.getDefault(), "SELECT  *  FROM items WHERE id = %d", id);
    }

    public static String selectItems(LocalDate firstDate, LocalDate lastDate) {
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE targetDate >= %d AND targetDate <= %d",
                firstDate.toEpochDay(), lastDate.toEpochDay());
    }
    public static String selectItems(LocalDate firstDate, LocalDate lastDate, Type type) {
        return String.format(Locale.getDefault(), "SELECT * FROM items WHERE targetDate >= %d AND targetDate <= %d AND type = %d ORDER by targetDate DESC",
                firstDate.toEpochDay(), lastDate.toEpochDay(), type.ordinal());
    }

    public static String selectItems(LocalDate firstDate, LocalDate lastDate, State state) {
        LocalDateTime localDateTimeFirst = firstDate.atStartOfDay();
        long startEpoch = localDateTimeFirst.toEpochSecond(ZoneOffset.UTC);
        long endEpoch = lastDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) + (3600 * 24);
        return String.format(Locale.ENGLISH, "SELECT * FROM items WHERE " +
                        "(state = %d AND  updated >= %d AND updated <= %d) ORDER BY updated DESC",
                state.ordinal(), startEpoch, endEpoch);
    }

    public static String selectMental(Item item) {
        return String.format(Locale.getDefault(), "SELECT * FROM mental WHERE itemID = %d", item.getID());
    }

    public static String selectMentals(LocalDate firstDate, LocalDate lastDate, boolean includeTemplates, boolean isDone) {
        return String.format(Locale.getDefault(), "SELECT * FROM mental WHERE date >= %d AND date <= %d AND isDone = 1 ORDER BY date DESC",
                firstDate.toEpochDay(),
                lastDate.toEpochDay());
        //includeTemplates ? 1: 0);
    }

    public static String selectTopTen() {
        return "SELECT * FROM mental ORDER BY energy DESC LIMIT 10";
    }

    public static String selectMentalTopTen(TopTenFragment.Mode mode) {
        return String.format("SELECT * FROM mental ORDER BY %s DESC LIMIT 10", mode.toString().toLowerCase());
    }

    public static String selectChildren(long id) {
        return String.format(Locale.getDefault(), "SELECT * FROM items WHERE parentID = %d", id);
    }

    /**
     * returns a sql queery, which selects all mentals of said date, done or not
     * @param date, the date which is of interest
     * @param isTemplate, not in use, remove ? TODO
     * @param isDone, set to true to get only those which are done the requested date
     * @return
     */
    public static String selectMentals(LocalDate date, boolean isTemplate, boolean isDone) {
        return String.format(Locale.getDefault(), "SELECT * FROM mental WHERE date = %d   AND  isDone = %d ORDER BY date DESC",
                date.toEpochDay(), 1);
    }


    public static String selectMentalsFromItem(LocalDate firstDate, LocalDate lastDate) {
        return String.format(Locale.getDefault(), "SELECT mental FROM items WHERE date >= %d AND date <= %d ORDER BY date DESC",
                firstDate.toEpochDay(), lastDate.toEpochDay());
    }
    public static String selectMentalsFromItems(LocalDate date, State state) {
        log("...selectMentalsFromItems(LocalDate");
        long startOfDay = date.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        long endOfDay = startOfDay + ( 3600 * 24);
        return String.format(Locale.getDefault(), "SELECT mental FROM items WHERE updated >= %d AND updated <= %d  AND state = %d",
                startOfDay, endOfDay, state.ordinal());
    }

    public static String selectItems(Week week) {
        return String.format(Locale.getDefault(), "SELECT * FROM items WHERE targetDate >= %d AND targetDate <= %d ORDER by targetDate DESC",
                week.getMonday().toEpochDay(), week.getLastDateOfWeek().toEpochDay());
    }



    public static String selectCalendarMonth(YearMonth yearMonth) {
        LocalDate lastDate =  yearMonth.atEndOfMonth();
        LocalDate firstDate = yearMonth.atDay(1);
        return String.format(Locale.getDefault(), "SELECT * FROM items WHERE targetDate >= %d AND targetDate <= %d AND isCalenderItem = 1 ORDER by targetDate DESC",
                firstDate.toEpochDay(), lastDate.toEpochDay());

    }

    public static String selectCalenderItems(LocalDate date) {
        return String.format(Locale.getDefault(),
                "SELECT * FROM items WHERE targetDate = %d AND isCalenderItem = 1 ORDER BY targetTime DESC",
                date.toEpochDay());

    }

    public static String selectMentalChildren(Item item) {
        return String.format(Locale.getDefault(), "SELECT * FROM mentals WHERE itemID = %d ORDER BY date DESC",
                item.getID()
                );
    }
}


