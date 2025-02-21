package se.curtrune.lucy.persist;

import static se.curtrune.lucy.app.Settings.Root.APPOINTMENTS;
import static se.curtrune.lucy.app.Settings.Root.DAILY;
import static se.curtrune.lucy.app.Settings.Root.PANIC;
import static se.curtrune.lucy.app.Settings.Root.PROJECTS;
import static se.curtrune.lucy.app.Settings.Root.THE_ROOT;
import static se.curtrune.lucy.app.Settings.Root.TODO;
import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.workers.NotificationsWorker;
import se.curtrune.lucy.workers.RepeatWorker;

public class ItemsWorker {
    public static boolean VERBOSE = false;

    /**
     * spawns an item based on it's template Item
     * @param template, the template to use
     * @return a brand new spanking item instance, b
     */
    private static Item createActualItem(Item template){
        log("...createActual(Item)", template.getHeading());
        Item item = new Item();
        item.setType(item.getType().ordinal());
        item.setHeading(template.getHeading());
        item.setComment(template.getComment());
        item.setDuration(template.getDuration());
        item.setCategory(template.getCategory());
        item.setTags(template.getTags());
        item.setTargetTime(template.getTargetTime());
        item.setTargetDate(LocalDate.now());
        item.setIsCalenderItem(template.isCalenderItem());
        //item.setEstimate(template.getEstimate());
        item.setColor(template.getColor());
        item.setState(State.DONE);
        item.setType(Type.TEMPLATE_CHILD);
        item.setAnxiety(template.getAnxiety());
        item.setEnergy(template.getEnergy());
        item.setMood(template.getMood());
        item.setStress(template.getStress());
        return item;
    }


    /**
     * deletes an item, but if it has children, //TODO delete children recursively
     * @param item, the item to delete from the db
     * @param context context context context
     * @return true if item was deleted false otherwise
     */
    public static boolean delete(Item item, Context context) {
        if( VERBOSE) log("ItemsWorker.delete(Item, Context)", item.getHeading());
        boolean res = false;
/*        if( item.hasChild()){
            log("item has child");
            Toast.makeText(context, "unlink not implemented", Toast.LENGTH_LONG).show();
        }else{*/
        int rowsAffected;
        try (LocalDB db = new LocalDB(context)) {
            rowsAffected = db.delete(item);
        }
        if( rowsAffected != 1){
            log("ERROR deleting item ", item.getID());
        }else{
            res = true;
        }
        //}
        return res;
    }

    /**
     * @param item, the item for which you wish to get its parent
     * @param context context context context
     * @return the parent, TODO, null if not parent?
     * MIGRATED TO REPOSITORY
     */
    public static Item getParent(Item item, Context context) {
        if( VERBOSE) log("ItemsWorker.getParent(Item, Context)");
        if( item == null){
            log("WARNING, parent item is null, returning null");
            return null;
        }
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItem(item.getParentId());
        }
    }

    public static Item getRootItem(Settings.Root root, Context context){
        if( VERBOSE) log("ItemsWorker.getRootItem(Settings.Root, Context)");
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
        if( VERBOSE) log("root id ", rootID);
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItem(rootID);
        }
    }

    /**
     * if item hasRepeat, creates instance up until RepeatWorker.maxDate
     * return the the template
     * if item has notification, sets notification
     * @param item, the item to be inserted
     * @param context context context
     * @return the inserted item, with its id field set to whatever db decided, autoincrement
     */
    public static Item insert(Item item, Context context)  {
        if( VERBOSE) log("ItemsWorker.insert(Item, Context)", item.getHeading());
        if( item.hasNotification()){
            NotificationsWorker.setNotification(item, context);
        }
        if (item.hasPeriod()){
            return insertRepeat(item, context);
        }else {
            try (LocalDB db = new LocalDB(context)) {
                item = db.insert(item);
            }
        }
        return item;
    }

    //MIGRATED
    public  static Item insertChild(Item parent, Item child, Context context)  {
        if(VERBOSE)log("ItemsWorker.insertChild(Item, Item, Context)");
        if( !parent.hasChild()){
            log("....no children for this parent, yet, parent: ", parent.getHeading());
            setHasChild(parent, true, context);
        }
        child.setParentId(parent.getID());
        if(child.hasPeriod()){
            return insertRepeat(child, context);
        }
        try (LocalDB db = new LocalDB(context)) {
            return db.insert(child);
        }
    }

    /**
     * @param template, the template to use for creating instances
     * @param context, context
     * @return the saved item with id
     */
    public static Item insertRepeat(Item template, Context context){
        log("ItemsWorker.insertRepeat(Item, Context)", template.getHeading());
        return RepeatWorker.insertItemWithRepeat(template, context);
    }

    /**
     * selects all the items
     * @param context, context context
     * @return all the items in the table items
     */
    public static List<Item> selectItems(Context context) {
        if(VERBOSE) log("ItemsWorker.selectItems(Context");
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
    @Deprecated
    public static List<Item> selectItems(LocalDate firstDate, LocalDate lastDate, Context context) {
        if(VERBOSE)log("ItemsWorker.selectItems(LocalDate, LocalDate, Context");
        try (LocalDB db = new LocalDB(context)) {
            String query = Queeries.selectItems(firstDate, lastDate, State.DONE);
            return db.selectItems(query);
        }
    }

    /**
     * selects all the items with type set to Appointment
     * @param context context context
     * @return all the items matching
     */
    public static List<Item> selectAppointments(Context context) {
        if(VERBOSE) log("ItemsWorker.selectAppointments(Context)");
        String query = Queeries.selectAppointments();
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(query);
        }
    }
    public static List<Item> selectAppointments(LocalDate date, Context context) {
        if(VERBOSE) log("ItemsWorker.selectAppointments(LocalDate, Context)");
        String query = Queeries.selectAppointments(date);
        Queeries.selectCalenderItems(date);
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(query);
        }
    }

    public static List<Item> selectChildren(Item parent, Context context){
        if(VERBOSE) log("ItemsWorker.selectChildren(Item, Context)");
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(Queeries.selectChildren(parent));
        }
    }
    @Deprecated
    public static List<Item> selectTemplateChildren(Item parent, Context context){
        try(LocalDB db = new LocalDB(context)){
            return db.selectItems(Queeries.selectTemplateChildren(parent));
        }
    }

    //MIGRATED TO REPOSITORY
    public static Item selectItem(long id, Context context) {
        log("ItemsWorker.selectItem(long, Context)");
        Item item = null;
        try(LocalDB db = new LocalDB(context)){
            item = db.selectItem(id);
        }catch (Exception e){
            log("EXCEPTION", e.getMessage());
        }
        return item;
    }
    /**
     *
     * @param date, the date
     * @param state , the state
     * @param context, context context
     * @return a list of items
     */
    @Deprecated
    public static List<Item> selectItems(LocalDate date, State state, Context context){
        if(VERBOSE) log("ItemsWorker.selectItems(Date, State, Context)");
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(Queeries.selectItems(date, state));
        }
    }

    public static List<Item> selectItems(State state, Context context) {
        if(VERBOSE) log("ItemsWorker.selectItems(State state)", state.toString());
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(Queeries.selectItems(state));
        }
    }

    public static List<Item> selectItems(LocalDate date, Type type, Context context) {
        if(VERBOSE) log("ItemsWorker.selectItems(LocalDate, Type, Context)", type.toString());
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(Queeries.selectItems(date, type));
        }
    }

    /**
     * not in use right now, but i suspect it will be need some time in the not so distant future
     * @param type the type to be selected,
     * @param context context context context
     * @return a list of item of said type
     */
    public static List<Item> selectItems(Type type, Context context) {
        String query = Queeries.selectItems(type);
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(query);
        }
    }
    public static List<Repeat> selectRepeats(Context context){
        log("ItemsWorker.selectRepeats()");
        String queery = Queeries.selectRepeats();
        try(LocalDB db = new LocalDB(context)){
            return  db.selectRepeats();
        }
    }

    /**
     * calenderItems, done items and possibly targetDate == date
     * @param date, the date for which to select items
     * @param context context context context
     * @return a list of items
     */
    public static List<Item> selectTodayList(LocalDate date, Context context){
        if( VERBOSE) log("ItemsWorker.selectTodayList(LocalDate, Context)", date.toString());
        String query = Queeries.selectTodayList(date);
        try (LocalDB db = new LocalDB(context)) {
            return db.selectItems(query);
        }
    }
    public static void setHasChild(Item item, boolean hasChild, Context context) {
        if(VERBOSE) log("ItemsWorker.setHasChild(Item, Context)", item.getHeading());
        try (LocalDB db = new LocalDB(context)) {
            db.setItemHasChild(item.getID(), hasChild);
        }
    }

    public static Item getAppointmentsRoot(Context context){
        Settings settings = Settings.getInstance(context);
        long id = settings.getRootID(APPOINTMENTS);
        try(LocalDB db = new LocalDB( context)) {
            return db.selectItem(id);
        }
    }

    public static Item getPanicRoot(Context context) {
        Settings settings = Settings.getInstance(context);
        long id = settings.getRootID(PANIC);
        try(LocalDB db = new LocalDB( context)) {
            return db.selectItem(id);
        }
    }
    public static Item getProjectsRoot(Context context){
        Settings settings = Settings.getInstance(context);
        long id = settings.getRootID(PROJECTS);
        try(LocalDB db = new LocalDB( context)) {
            return db.selectItem(id);
        }
    }

    @Deprecated
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

    public static List<Item> selectIsCalenderItems(YearMonth yearMonth, Context context) {
        if (VERBOSE) log("ItemsWorker.selectCalenderItems(YearMonth)");
        try (LocalDB db = new LocalDB(context)) {
            String queery = Queeries.selectIsCalendarItems(yearMonth);
            return db.selectItems(queery);
        }
    }

    public static List<Item> selectAppointments(YearMonth yearMonth, Context context) {
        if (VERBOSE) log("ItemsWorker.selectAppointments(YearMonth)", yearMonth.toString());
        try (LocalDB db = new LocalDB(context)) {
            String queery = Queeries.selectAppointments(yearMonth);
            return db.selectItems(queery);
        }
    }

    public static List<Item> selectItems(Week week, Context context) {
        if(VERBOSE) log("ItemsWorker.selectItems(Week, Context)");
        String queery = Queeries.selectItems(week);
        List<Item> items;
        try(LocalDB db = new LocalDB(context)){
            items = db.selectItems(queery);
        }
        return items;
    }

    public static List<Item> selectCalenderItems(LocalDate currentDate, Context context) {
        if( VERBOSE) log("ItemsWorker.selectCalenderItems(LocalDate, Context)");
        String queery = Queeries.selectCalenderItems(currentDate);
        if(VERBOSE) log("queery", queery);
        try(LocalDB db = new LocalDB(context)){
            return db.selectItems(queery);
        }
    }
    public  static void touchParents(Item item, Context context){
        if(VERBOSE) log("ItemsWorker.touchParents()");
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
    public static int update(Repeat repeat, Context context){
        log("ItemsWorker.update(Repeat, Context)");
        try(LocalDB db = new LocalDB(context)){
            return db.update(repeat);
        }
    }

    /**
     * does loads of things, not clean code
     * it updates the template, spawns stuff if the template item is done, otherwise it just updates the template
     * creates actual item and actual mental, using the template item
     * @param template, the template to user
     * @param context, context bloody context
     * @return 1 if success otherwise some kind of an error has occurred
     */
    @Deprecated
    private static int updateTemplate(Item template, Context context) {
        log("ItemsWorker.updateTemplate(Item, Context)", template.getHeading());
        try (LocalDB db = new LocalDB(context)) {
            if (template.isDone()) {
                if (VERBOSE) log("...template is done, will spawn a child");
                template.setState(State.TODO);
                Item child = createActualItem(template);
                child.setType(Type.TEMPLATE_CHILD);
                child = db.insertChild(template, child);//creates and inserts mental, or rather insert(Item) does
                if (template.hasPeriod()) {
                    template.updateTargetDate();
                }else{
                    template.setTargetDate(LocalDate.now());
                }
                template.setDuration(0);
            }
            return db.update(template);
        }
    }

    public static List<Item> selectEvents(Week week, Context context) {
        log("ItemsWorker.selectEvents(Week, Context)");
        String queery = Queeries.selectEvents(week);
        List<Item> items;
        try(LocalDB db = new LocalDB(context)){
            items = db.selectItems(queery);
        }
        return items;
    }

    public static List<Item> selectItems(LocalDate date, Context context) {
        if( VERBOSE )log("ItemsWorker.selectItems(LocalDate, Context)");
        String queery = Queeries.selectItems(date);
        List<Item> items;
        try(LocalDB db = new LocalDB(context)){
            items = db.selectItems(queery);
        }
        return items;
    }


    /**
     * deletes parent Item and all of its children
     * @param parent, parent Item
     * @param context, context
     */
    public static void deleteTree(Item parent, Context context) {
        log("ItemsWorker.deleteTree(Item, Context)", parent.getHeading());
        if( parent.hasChild()){
            List<Item> children = ItemsWorker.selectChildren(parent, context);
            for(Item item: children){
                deleteTree(item, context);
            }
        }
        log("DELETE ME", parent.getHeading());
        boolean stat = ItemsWorker.delete(parent, context);
        if(!stat){
            log("ERROR DELETING ITEM");
        }
    }

    public static void insert(List<Item> items, Context context) {
        log("ItemsWorker(List<Item>, Context)");
        try(LocalDB db = new LocalDB(context)){
            db.insert(items);
        }
    }

    /**
     *
     * @param repeat, the instance of repeat to be inserted into the repeat table
     * @param context, context, context
     * @return the inserted Repeat or null if insert failed
     */
    public static Repeat insert(Repeat repeat, Context context) {
        log("ItemsWorker.insert(Repeat, Context)");
        try(LocalDB db = new LocalDB(context)){
            repeat = db.insert(repeat);
        }catch (Exception e){
            log("EXCEPTION", e.getMessage());
            return null;
        }
        return repeat;
    }

    public static Repeat selectRepeat(long id, Context context) {
        log("ItemsWorker.selectRepeat(long, Context)");
        try(LocalDB db = new LocalDB(context)){
            return db.selectRepeat(id);
        }
    }
}
