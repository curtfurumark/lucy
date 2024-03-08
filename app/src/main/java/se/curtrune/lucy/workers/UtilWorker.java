package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import se.curtrune.lucy.persist.LocalDB;

public class UtilWorker {
    public static String[] getCategories(Context context){
        log("UtilWorker.getCategories(Context)");
        LocalDB db = new LocalDB(context);
        return  db.getCategories();

    }
}
