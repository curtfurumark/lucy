package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.widget.Toast;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.activities.ItemSession;
import se.curtrune.lucy.activities.Settings;
import se.curtrune.lucy.activities.TodayActivity;
import se.curtrune.lucy.classes.App;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.persist.Queeries;

public class ItemsWorker {
    private static ItemsWorker instance;
    private ItemsWorker(){
        log("...ItemsWorker() private constructor");
    }
    public static ItemsWorker getInstance(){
        if( instance == null){
            instance = new ItemsWorker();
        }
        return instance;
    }

    public static Item getParent(Item currentParent, Context context) {
        log("ItemsWorker.getParent(Item, Context)");
        LocalDB db = new LocalDB(context);
        return db.selectItem(currentParent.getParentId());
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

    public static boolean hasChild(long parentId, Context context) {
        log("ItemsWorker.hasChild(long) parentID", parentId );
        LocalDB db = new LocalDB(context);
        return  db.selectItem(parentId).hasChild();
    }

    public static List<Item> selectItems(Context context) {
        log("ItemsWorker.selectItems(Context");
        LocalDB db = new LocalDB(context);
        return db.selectItems();
    }

    public static Item getTodayParent(Context context) {
        log("ItemsWorker.getTodayParent(Context context)");
        LocalDB db = new LocalDB(context);
        return db.selectItem(App.getTodoListID());
    }

    private Item createChildToInfinite(Item parent){
        log("...createChildToInfinite(Item item)");
        Item child = new Item();
        child.setParentId(parent.getID());
        child.setHeading(parent.getHeading());
        child.setState(State.DONE);
        return child;
    }

    /**
     * sets targetDate of infiniteItem to 'today + item.getDays'
     * @param item, is an Item with State.INFINITE
     * @param context,
     * @throws SQLException
     */
    public void handleInfinite(Item item, Context context) throws SQLException {
        log("...handleInfinite(Item, Context)", item.getHeading());
        Item child = createChildToInfinite(item);
        child = insert(child, context);
        item.setTargetDate(LocalDate.now().plusDays(item.getDays()));
        update(item, context);

    }
    public static Item insert(Item item, Context context) throws SQLException {
        log("ItemsWorker.insert(Item, Context");
        LocalDB db = new LocalDB(context);
        item = db.insert(item);
        db.close();
        return item;
    }
    private static Item insertChild(Item parent, Item child, Context context) throws SQLException {
        log("ItemsWorker.insertChild(Item, Item, Context)");
        if( !parent.hasChild()){
            //TODO: set hasChild to true
        }
        LocalDB db = new LocalDB(context);
        return db.insert(child);
    }

    private Mental insert(Mental mental, Context context) throws SQLException {
        log("ItemsWorker.insert(Mental, Context)");
        LocalDB db = new LocalDB(context);
        return db.insert(mental);
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
    public static List<Item> selectItems(Type type, TodayActivity context) {
        LocalDB db = new LocalDB(context);
        return db.selectItems(type);
    }

    public static List<Item> selectItems(LocalDate date, Context context, State state){
        log("ItemsWorker.selectItems(LocalDate, Context, State");
        LocalDB db = new LocalDB(context);
        List<Item> items = db.selectItems(date, state);
        return items;
    }

    public List<Item> selectChildItems(Item parent ,Context context) {
        log("ItemsWorker.selectChildItems(Item, Context)", parent.getHeading());
        LocalDB db = new LocalDB(context);
        return db.selectItems(Queeries.selectChildren(parent));
    }
    public static List<Item> selectTodayList(LocalDate date, Context context){
        log("...selectTodayList(LocalDate, Context)", date.toString());
        String query = Queeries.selectTodayList(date);
        LocalDB db = new LocalDB(context);
        return db.selectItems(query);


    }
    public static void setHasChild(Item item, boolean hasChild, Context context) {
        log("ItemsWorker.setHasChild(Item, Context)");
        LocalDB db = new LocalDB(context);
        db.setItemHasChild(item.getID(), hasChild);
    }
    public static void setHasChild(long id, boolean hasChild, Context context){
        log("ItemsWorker.setHasChild(long, boolean, Context");
        LocalDB db = new LocalDB(context);
        db.setItemHasChild(id, hasChild);
    }

    public void setItemState(Item item, State state, Context context) throws SQLException {
        log("ItemsWorker.setItemState(Item, State, Context)");
        if( item.isInfinite()){
            handleInfinite(item, context);
        }else {
            log("...item is not infinite");
            item.setState(state);
            LocalDB db = new LocalDB(context);
            int rowsAffected = db.update(item);
            log("...rowsAffected", rowsAffected);
        }
    }

    public void touch(Item currentItem, Context context) {
        log("ItemsWorker.touch(Item, Context)");
        LocalDB db = new LocalDB(context);
        db.touch(currentItem);
    }
    public int update(Item item, Context context) {
        log("ItemsWorker.update(Item, Context)");
        LocalDB db = new LocalDB(context);
        return db.update(item);
    }
}
