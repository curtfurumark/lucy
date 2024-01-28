package se.curtrune.lucy.util;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import se.curtrune.lucy.activities.ItemEditor;
import se.curtrune.lucy.persist.LocalDB;

public class Settings {
    public static boolean INITIALIZED = false;
    public static final int MOOD_OFFSET = 5;
    public static final int ENERGY_OFFSET = 5;
    public static String[] CATEGORIES = new String[] {"household", "work", "health", "play"};

    public static String[] getCategories(Context context) {
        log("Settings.getCategories(Context)");
        LocalDB db = new LocalDB(context);
        return db.getCategories();
    }
    private void initTableCategories(){
        log("...initTableCategories()");

    }
    public void initTheApp(Context context){
        log("Settings.initTheApp(Context)");
        initTableCategories();
    }
}
