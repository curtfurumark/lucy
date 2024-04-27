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

    private static Item createTemplateChild(Item parent){
        log("...createTemplateChild(Item item)");
        Item child = new Item();
        child.setParentId(parent.getID());
        child.setHeading(parent.getHeading());
        child.setState(State.DONE);
        return child;
    }

    public static boolean delete(Item item, Context context) {
        log("LocalDB.delete(Item, Context)");
        boolean res = false;
        if( item.hasChild()){
            Toast.makeText(context, "unlink not implemented", Toast.LENGTH_LONG).show();
        }else{
            LocalDB db = new LocalDB(context);
            int rowsAffected = db.delete(item);
            if( rowsAffected != 1){
                Toast.makeText(context, "error deleting item", Toast.LENGTH_LONG).show();
            }else{
                res = true;
            }
        }
        return res;
    }
    public static List<Item> getAllChildren(Item parent, Context context){
        List<Item> items;
        List<Item> children = selectChildren(parent, context);
        for(Item item: children){


        }
        return children;
    }

    public static Item getParent(Item currentParent, Context context) {
        log("ItemsWorker.getParent(Item, Context)");
        if( currentParent == null){
            log("...currentParent is null, returning null");
            return null;
        }
        LocalDB db = new LocalDB(context);
        return db.selectItem(currentParent.getParentId());
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
        LocalDB db = new LocalDB(context);
        return db.selectItem(rootID);
    }

    public static Item getTodayParent(Context context) {
        if( VERBOSE)log("ItemsWorker.getTodayParent(Context context)");
        LocalDB db = new LocalDB(context);
        Settings settings = Settings.getInstance(context);
        return db.selectItem(settings.getRootID(DAILY));
    }

    public static boolean hasChild(long parentId, Context context) {
        log("ItemsWorker.hasChild(long) parentID", parentId );
        LocalDB db = new LocalDB(context);
        return  db.selectItem(parentId).hasChild();
    }



    public static Item selectItem(long parentId, Context context) {
        log("...selectItem(long, Context");
        LocalDB db = new LocalDB(context);
        return db.selectItem(parentId);
    }

    public static List<Item> selectItems(Context context) {
        log("ItemsWorker.selectItems(Context");
        LocalDB db = new LocalDB(context);
        return db.selectItems();
    }



    public static List<Item> selectItems(LocalDate firstDate, LocalDate lastDate, Context context) {
        log("...selectItems(LocalDate, LocalDate, Context");
        LocalDB db = new LocalDB(context);
        String query = Queeries.selectItems(firstDate, lastDate, State.DONE);
        return db.selectItems(query);
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
        LocalDB db = new LocalDB(context);
        return db.insert(child);
    }

    public static List<Item> selectAppointments(Context context) {
        log("ItemsWorker.selectAppointments(Context)");
        String query = Queeries.selectAppointments();
        LocalDB db = new LocalDB(context);
        return db.selectItems(query);
    }
    public static List<Item> selectAppointments(LocalDate date, Context context) {
        log("ItemsWorker.selectAppointments(LocalDate, Context)", date.toString());
        String queery = Queeries.selectAppointments(date);
        LocalDB db = new LocalDB(context);
        return db.selectItems(queery);
    }
    public static List<Item> selectChildren(Item item, Context context){
        log("ItemsWorker.selectChildren(Item, Context)");
        LocalDB db = new LocalDB(context);
        return db.selectChildren(item);
    }
    public static List<Item> selectDateState(LocalDate date, State state, Context context){
        log("ItemsWorker.selectDateState()");
        LocalDB db = new LocalDB(context);
        return  db.selectItems(Queeries.selectItems(date, state));
    }
    public static List<Item> selectItems(State state, Context context) {
        log("ItemsWorker.selectItems(State state)", state.toString());
        LocalDB db = new LocalDB(context);
        return db.selectItems(Queeries.selectItems(state));
    }
    public static List<Item> selectItems(Type type, Context context) {
        LocalDB db = new LocalDB(context);
        return db.selectItems(type);
    }

    public static List<Item> selectItems(LocalDate date, Context context, State state){
        log("ItemsWorker.selectItems(LocalDate, Context, State");
        LocalDB db = new LocalDB(context);
        List<Item> items = db.selectItems(date, state);
        return items;
    }

    public static List<Item> selectChildItems(Item parent, Context context) {
        log("ItemsWorker.selectChildItems(Item, Context)", parent.getHeading());
        LocalDB db = new LocalDB(context);
        return db.selectItems(Queeries.selectChildren(parent));
    }
    public static List<Item> selectChildren(long id, Context context) {
        log("...selectChildren(long, Context)", id);
        LocalDB db = new LocalDB(context);
        return db.selectItems(Queeries.selectChildren(id));
    }
    public static List<Item> selectTodayList(LocalDate date, Context context){
        log("ItemsWorker.selectTodayList(LocalDate, Context)", date.toString());
        String query = Queeries.selectTodayList(date);
        LocalDB db = new LocalDB(context);
        return db.selectItems(query);
    }
    public static void setHasChild(Item item, boolean hasChild, Context context) {
        log("ItemsWorker.setHasChild(Item, Context)", item.getHeading());
        LocalDB db = new LocalDB(context);
        db.setItemHasChild(item.getID(), hasChild);
    }
    public static void setHasChild(long id, boolean hasChild, Context context){
        log("ItemsWorker.setHasChild(long, boolean, Context");
        LocalDB db = new LocalDB(context);
        db.setItemHasChild(id, hasChild);
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
        LocalDB db = new LocalDB( context);
        return db.selectItem(id);
    }
    public static Item getDailyRoot(Context context){
        Settings settings = Settings.getInstance(context);
        long id = settings.getRootID(DAILY);
        LocalDB db = new LocalDB( context);
        return db.selectItem(id);
    }

    public static List<Item> selectCalenderItems(YearMonth yearMonth, Context context) {
        log("...selectCalenderItems(YearMonth)");
        LocalDate firstDate = yearMonth.atDay(1);
        LocalDate lastDate = yearMonth.atEndOfMonth();
        LocalDB db = new LocalDB(context);
        String queery = Queeries.selectItems(firstDate, lastDate,Type.APPOINTMENT);
        return db.selectItems(queery);
    }


    public void touch(Item currentItem, Context context) {
        log("ItemsWorker.touch(Item, Context)");
        LocalDB db = new LocalDB(context);
        db.touch(currentItem);
    }
    public  static void touchParents(Item item, Context context){
        log("...touchParents()");
        LocalDB db = new LocalDB(context);
        db.touchParents(item);
    }

    /**
     * if Item is template, lots of things happen, or should happen, whatever
     * if Item is not a template, update updates the items updated field, and nothing else
     * anything else is the responsibility of the caller
     * @param item, the item to be updated,
     * @param context
     * @return, rows affected
     */
    public static int update(Item item, Context context) {
        log("ItemsWorker.update(Item, Context)");
        if(item.isTemplate() && item.isDone()){
            return updateTemplate(item, context);
        }else {
            LocalDB db = new LocalDB(context);
            item.setUpdated(LocalDateTime.now());
            return db.update(item);
        }
    }
    private static int updateTemplate(Item template, Context context) {
        log("...updateTemplate(Item, Context)", template.getHeading());
        if(template.isDone()){
            log("...do not set templates to done");
            template.setState(State.TODO);
        }
        Item child = new Item();
        child.setState(State.DONE);
        child.setType(Type.NODE);//TODO , type generated ? .
        child.setHeading(template.getHeading());
        child.setDuration(template.getDuration());
        child.setCategory(template.getCategory());
        child.setTags(template.getTags());
        child.setMental(template.getMental());
        child.setTargetTime(LocalTime.now());
        child.setMental(template.getMental());
        LocalDB db = new LocalDB(context);
        child = db.insertChild(template, child);
        if( template.hasMental()){//inherit the template
            Mental childMental = new Mental(template.getMental());
            long itemID = child.getID();
            log("...itemID", itemID);
            childMental.setItemID(child.getID());
            childMental.setHeading(template.getHeading());
            childMental.setDate(LocalDate.now());
            childMental.setTime(LocalTime.now());
            childMental.setUpdated(LocalDateTime.now());
            childMental = MentalWorker.insert(childMental, context);
            log(childMental);
        }
        if( template.hasPeriod()) {
            template.updateTargetDate();
        }
        template.setDuration(0);
        return db.update(template);
    }
}
