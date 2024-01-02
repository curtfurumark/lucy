package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.widget.Toast;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.activities.ItemSession;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.persist.Queeries;

public class ItemsWorker {
    private static ItemsWorker instance;
    private ItemsWorker(){

    }
    public static ItemsWorker getInstance(){
        if( instance == null){
            instance = new ItemsWorker();
        }
        return instance;
    }

    public void delete(Item item, Context context) {
        log("LocalDB.delete(Item, Context)");
        if( item.hasChild()){
            Toast.makeText(context, "unlink not implemented", Toast.LENGTH_LONG).show();
        }else{
            LocalDB db = new LocalDB(context);
            int rowsAffected = db.delete(item);
            if( rowsAffected != 1){
                Toast.makeText(context, "error deleting item", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static Item insert(Item item, Context context) throws SQLException {
        log("ItemsWorker.insert(Item, Context");
        LocalDB db = new LocalDB(context);
        item = db.insert(item);
        db.close();
        return item;
    }

    private Mental insert(Mental mental, Context context) throws SQLException {
        log("ItemsWorker.insert(Mental, Context)");
        LocalDB db = new LocalDB(context);
        return db.insert(mental);


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

    public static List<Item> selectItems(LocalDate date, Context context, State state){
        log("ItemsWorker.selectItems(LocalDate, Context, State");
        LocalDB db = new LocalDB(context);
        List<Item> items = db.selectItems(date, state);
        return items;
    }


    public List<Item> selectChildItems(Item parent ,Context context) {
        log("ItemsWorker.selectChildItems(Item, Context");
        LocalDB db = new LocalDB(context);
        return db.selectItems(Queeries.selectChildren(parent));
    }



    public int update(Item item, Context context) {
        log("ItemsWorker.update(Item, Context)");
        LocalDB db = new LocalDB(context);
        return db.update(item);
    }

    public void setHasChild(Item item, Context context) {
        log("ItemsWorker.setHasChild(Item, Context)");
        LocalDB db = new LocalDB(context);
        db.setItemHasChild(item.getID());
    }

    public int  setItemState(Item item, State state, Context context) {
        log("ItemsWorker.setItemState(Item, State, Context)");
        item.setState(state);
        LocalDB db = new LocalDB(context);
        return db.update(item);
    }
}
