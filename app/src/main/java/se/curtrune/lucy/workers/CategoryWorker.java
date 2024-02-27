package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.util.List;

import se.curtrune.lucy.persist.LocalDB;

public class CategoryWorker {
    public static void insertCategory(String category, Context context){
        log("...insertCategory(String category)", category);
        LocalDB db = new LocalDB(context);
        db.insertCategory(category);
    }

    public static String[] getCategories(Context context) {
        log("...getCategories()");
        LocalDB db = new LocalDB(context);
        return db.getCategories();
    }
}
