package se.curtrune.lucy.workers;

import static se.curtrune.lucy.app.Settings.Root.APPOINTMENTS;
import static se.curtrune.lucy.app.Settings.Root.DAILY;
import static se.curtrune.lucy.app.Settings.Root.PANIC;
import static se.curtrune.lucy.app.Settings.Root.PROJECTS;
import static se.curtrune.lucy.app.Settings.Root.THE_ROOT;
import static se.curtrune.lucy.app.Settings.Root.TODO;
import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;

public class ItemsWorker {
    private static ItemsWorker instance;
    public static boolean VERBOSE = false;
    private ItemsWorker(){
        log("...ItemsWorker() private constructor");
    }
    public static ItemsWorker getInstance(){
        if( instance == null){
            instance = new ItemsWorker();
        }
        return instance;
    }

    public static long  calculateEstimate(List<Item> items) {
        log("ItemsWorker.calculateEstimate(List<Item>)");
        return items.stream().mapToLong(Item::getEstimatedDuration).sum();
    }

    public static boolean delete(Item item, Context context) {
        log("LocalDB.delete(Item, Context)");
        boolean res = false;
        if( item.hasChild()){
            Toast.makeText(context, "unlink not implemented", Toast.LENGTH_LONG).show();
        }else{
            int rowsAffected;
            try (LocalDB db = new LocalDB(context)) {
                rowsAffected = db.delete(item);
            }
            if( rowsAffected != 1){
                Toast.makeText(context, "error deleting item", Toast.LENGTH_LONG).show();
            }else{
                res = true;
            }
        }
        return res;
    }

    public static Item getParent(Item currentParent, Context context) {
        log("ItemsWorker.getParent(Item, Context)");
        if( currentParent == null){
            log("...currentParent is null, returning null");
            return null;
        }
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItem(currentParent.getParentId());
        }
    }


    public static Item getRootItem(Settings.Root root, Context context){
        log("ItemsWorker.getRootItem(Settings.Root, Context)");
        Settings settings = Settings.getInstance(context);
        long rootID = -1;
        switch (root){
            case APPOINTMENTS:
                rootID = settings.getRootID(APPOINTMENTS);
                break;
            case TODO:
                rootID = settings.getRootID(TODO);
                break;
            case DAILY:
                rootID = settings.getRootID(DAILY);
                break;
            case PROJECTS:
                rootID = settings.getRootID(PROJECTS);
                break;
            case PANIC:
                rootID = settings.getRootID(PANIC);
                break;
            case THE_ROOT:
                rootID = settings.getRootID(THE_ROOT);
                break;
        }
        log("root id ", rootID);
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItem(rootID);
        }
    }

    public static Item insert(Item item, Context context)  {
        log("ItemsWorker.insert(Item, Context)", item.getHeading());
        LocalDB db = new LocalDB(context);
        item = db.insert(item);
        db.close();
        return item;
    }

    public  static Item insertChild(Item parent, Item child, Context context)  {
        log("ItemsWorker.insertChild(Item, Item, Context)");
        if( !parent.hasChild()){
            log("....not children for this parent, yet");
            setHasChild(parent, true, context);
        }
        child.setParentId(parent.getID());
        try (LocalDB db = new LocalDB(context)) {
            return db.insert(child);
        }
    }


    public static List<Item> selectItems(Context context) {
        log("ItemsWorker.selectItems(Context");
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems();
        }
    }

    /**
     * select done items as specified by argument
     * @param firstDate first date inclusive
     * @param lastDate last date inclusive
     * @param context just the frigging context
     * @return a list as specified
     */
    public static List<Item> selectItems(LocalDate firstDate, LocalDate lastDate, Context context) {
        log("...selectItems(LocalDate, LocalDate, Context");
        try (LocalDB db = new LocalDB(context)) {
            String query = Queeries.selectItems(firstDate, lastDate, State.DONE);
            return db.selectItems(query);
        }
    }

    public static List<Item> selectAppointments(Context context) {
        log("ItemsWorker.selectAppointments(Context)");
        String query = Queeries.selectAppointments();
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(query);
        }
    }
    public static List<Item> selectAppointments(LocalDate date, Context context) {
        log("ItemsWorker.selectAppointments(LocalDate, Context)", date.toString());
        String queery = Queeries.selectAppointments(date);
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(queery);
        }
    }
    public static List<Item> selectChildren(Item item, Context context){
        log("ItemsWorker.selectChildren(Item, Context)");
        try (LocalDB db = new LocalDB(context)) {
            return db.selectChildren(item);
        }
    }
    public static List<Item> selectDateState(LocalDate date, State state, Context context){
        log("ItemsWorker.selectDateState()");
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(Queeries.selectItems(date, state));
        }
    }
    public static List<Item> selectItems(State state, Context context) {
        log("ItemsWorker.selectItems(State state)", state.toString());
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(Queeries.selectItems(state));
        }
    }
    public static List<Item> selectItems(Type type, Context context) {
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(type);
        }
    }

    public static List<Item> selectItems(LocalDate date, Context context, State state){
        log("ItemsWorker.selectItems(LocalDate, Context, State");
        List<Item> items;
        try (LocalDB db = new LocalDB(context)) {
            items = db.selectItems(date, state);
        }
        return items;
    }

    public static List<Item> selectChildItems(Item parent, Context context) {
        log("ItemsWorker.selectChildItems(Item, Context)", parent.getHeading());
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(Queeries.selectChildren(parent));
        }
    }
    public static List<Item> selectChildren(long id, Context context) {
        log("...selectChildren(long, Context)", id);
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(Queeries.selectChildren(id));
        }
    }
    public static List<Item> selectTodayList(LocalDate date, Context context){
        log("ItemsWorker.selectTodayList(LocalDate, Context)", date.toString());
        String query = Queeries.selectTodayList(date);
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(query);
        }
    }
    public static void setHasChild(Item item, boolean hasChild, Context context) {
        log("ItemsWorker.setHasChild(Item, Context)", item.getHeading());
        try (LocalDB db = new LocalDB(context)) {
            db.setItemHasChild(item.getID(), hasChild);
        }
    }

    public static Item getAppointmentsRoot(Context context){
        Settings settings = Settings.getInstance(context);
        long id = settings.getRootID(APPOINTMENTS);
        LocalDB db = new LocalDB( context);
        return db.selectItem(id);
    }

    public static Item getPanicRoot(Context context) {
        Settings settings = Settings.getInstance(context);
        long id = settings.getRootID(PANIC);
        LocalDB db = new LocalDB( context);
        return db.selectItem(id);
    }
    public static Item getProjectsRoot(Context context){
        Settings settings = Settings.getInstance(context);
        long id = settings.getRootID(PROJECTS);
        LocalDB db = new LocalDB( context);
        return db.selectItem(id);
    }

    public static Item getTodoRoot(Context context){
        Settings settings = Settings.getInstance(context);
        long id = settings.getRootID(TODO);
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItem(id);
        }
    }
    public static Item getDailyRoot(Context context){
        Settings settings = Settings.getInstance(context);
        long id = settings.getRootID(DAILY);
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItem(id);
        }
    }

    public static List<Item> selectCalenderItems(YearMonth yearMonth, Context context) {
        log("...selectCalenderItems(YearMonth)");
        LocalDate firstDate = yearMonth.atDay(1);
        LocalDate lastDate = yearMonth.atEndOfMonth();
        LocalDB db = new LocalDB(context);
        String queery = Queeries.selectItems(firstDate, lastDate,Type.APPOINTMENT);
        return db.selectItems(queery);
    }

    public static List<Item> selectItems(Week week, Context context) {
        log("ItemsWorker.selectItems(Week, Context)");
        String queery = Queeries.selectItems(week);
        List<Item> items;
        try(LocalDB db = new LocalDB(context)){
            items = db.selectItems(queery);
        }
        return items;
    }


    public void touch(Item currentItem, Context context) {
        log("ItemsWorker.touch(Item, Context)");
        try (LocalDB db = new LocalDB(context)) {
            db.touch(currentItem);
        }
    }
    public  static void touchParents(Item item, Context context){
        log("...touchParents()");
        try (LocalDB db = new LocalDB(context)) {
            db.touchParents(item);
        }
    }

    /**
     * if Item is template, lots of things happen, or should happen, whatever
     * if Item is not a template, update updates the items updated field, and nothing else
     * anything else is the responsibility of the caller
     * @param item, the item to be updated,
     * @param context you guessed it
     * @return rows affected
     */
    public static int update(Item item, Context context) {
        log("ItemsWorker.update(Item, Context)", item.getHeading());
        if(item.isTemplate() && item.isDone()){
            return updateTemplate(item, context);
        }else {
            try (LocalDB db = new LocalDB(context)) {
                item.setUpdated(LocalDateTime.now());
                return db.update(item);
            }
        }
    }
    private static int updateTemplate(Item template, Context context) {
        log("...updateTemplate(Item, Context)", template.getHeading());
        try (LocalDB db = new LocalDB(context)) {
            if (template.isDone()) {
                if (VERBOSE) log("...template is done, will spawn a child");
                template.setState(State.TODO);
                Item child = new Item(template);
                child.setState(State.DONE);
                Mental mental = new Mental(template.getMental());
                mental.isDone(true);
                mental.setDate(LocalDate.now());
                mental.setTime(LocalTime.now());
                child.setMental(mental);
                child = db.insertChild(template, child);//creates and inserts mental, or rather insert(Item) does
                if (template.hasPeriod()) {
                    template.updateTargetDate();
                }
                template.setDuration(0);
    /*            Mental childMental = new Mental(template.getMental());
                assert  childMental != null;
                childMental.setDate(LocalDate.now());
                childMental.setTime(LocalTime.now());
                childMental.setUpdated(LocalDateTime.now());
                childMental.setCreated(LocalDateTime.now());
                childMental.setIsTemplate(false);
                childMental.setItemID(child.getID());
                childMental = MentalWorker.insert(childMental, context);*/
            }
            return db.update(template);
        }
    }
}
