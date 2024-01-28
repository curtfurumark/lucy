package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.media.Image;

import java.sql.SQLException;

import se.curtrune.lucy.persist.LocalDB;

public class App {
    public static void createDefaultLists(Context context) throws SQLException {
        log("...createDefaultLists(Context)");
        LocalDB db = new LocalDB(context);
        Item item = new Item("todo");
        item = db.insert(item);
    }
    public static long getTodoListID(){
        return 23;
    }
}
