package se.curtrune.lucy.app;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.content.res.Resources;

import androidx.fragment.app.Fragment;

import java.sql.SQLException;
import java.util.Arrays;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.persist.DBAdmin;

public class Lucinda {

    private static Lucinda instance;
    private final Settings settings;
    public static Item currentParent;
    public static Fragment currentFragment;
    public static boolean VERBOSE = false;


    public static String[] CATEGORIES = new String[] {"household", "work", "health", "play"};

    private Lucinda(Context context){
        log("Lucinda(Context)");
        settings = Settings.getInstance(context);
    }
    public static Lucinda getInstance(Context context){
        if( instance == null){
            instance = new Lucinda(context);
        }
        return instance;
    }
    public  void initialize(Context context) throws SQLException {
        log("Lucinda.initialize(Context)");
        initTheApp(context);
        settings.setLucyIsInitialized(true, context);
        log("...Lucinda is initialized");
    }


    public boolean isInitialized(Context context){
        log("Lucinda.isInitialized(Context)");
        return settings.isInitialized(context);
    }
    public void initTheApp(Context context) {
        log("Lucinda.initTheApp(Context)");
        DBAdmin.createTables(context);
        DBAdmin.insertCategories(context);
        DBAdmin.insertRootItems(context);
        setDefaultUserSettings(context);
    }


    public void reset(Context context) throws SQLException {
        log("Lucinda.reset(Context)");
        DBAdmin.dropTables(context);
        DBAdmin.createTables(context);
        DBAdmin.insertCategories(context);
        DBAdmin.insertRootItems(context);
        Demo.insertDemo(context);
        log("..almost done, possibly maybe");
        setDefaultUserSettings(context);
        settings.setLucyIsInitialized(true, context);
        settings.reload(context);
    }
    public static void setDefaultUserSettings(Context context){
        log("...setUserSettings()");
        User.setPanicUrls(context.getResources().getStringArray(R.array.panicUrls), context);
        User.setUsesPassword(false, context);
        User.setUseDarkMode(true, context);

    }
    public void setIsInitialized(boolean b, Context context) {
        log("Lucinda.setIsInitialized(boolean, Context");
        settings.setLucyIsInitialized(b, context);
    }
}
