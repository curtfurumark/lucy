package se.curtrune.lucy.app;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.sql.SQLException;

import se.curtrune.lucy.R;
import se.curtrune.lucy.persist.DBAdmin;

public class Lucinda {

    private static Lucinda instance;
    private final Settings settings;
    //public static Item currentParent;
    public static boolean VERBOSE = false;
    public static boolean Dev = false;
    private static final String KEY_NIGHTLY_ALARM ="KEY_NIGHTLY_ALARM";

    private Lucinda(Context context){
        if( VERBOSE) log("Lucinda(Context)");
        settings = Settings.getInstance(context);
    }
    public static Lucinda getInstance(Context context){
        if( instance == null){
            instance = new Lucinda(context);
        }
        return instance;
    }

    /**
     * returns info about this application
     * @param context, context context and more context
     * @return, PackageInfo or null if not package name found
     */
    public static PackageInfo getPackageInfo(Context context){
        log("Lucinda.getPackageInfo()");
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    public static void setNightlyAlarm(Context context) {
        log("...setNightlyAlarm(Context");
        //EasyAlarm easyAlarm = new EasyAlarm();
    }


    public  void initialize(Context context) throws SQLException {
        log("Lucinda.initialize(Context)");
        initTheApp(context);
        settings.setLucyIsInitialized(true, context);
        log("...Lucinda is initialized");
    }


    public boolean isInitialized(Context context){
        if( VERBOSE) log("Lucinda.isInitialized(Context)");
        return settings.isInitialized(context);
    }
    public void initTheApp(Context context) throws SQLException {
        log("Lucinda.initTheApp(Context)");
        DBAdmin.createTables(context);
        DBAdmin.insertRootItems(context);
        Demo.insertDemo(context);
        setDefaultUserSettings(context);
    }
    public static boolean nightlyAlarmIsSet(Context context) {
        log("Lucinda.nightlyAlarmIsSet(Context)");
        return Settings.getBoolean(KEY_NIGHTLY_ALARM, false, context);
    }

    public void reset(Context context) throws SQLException {
        log("Lucinda.reset(Context)");
        DBAdmin.dropTables(context);
        DBAdmin.createTables(context);
        DBAdmin.insertRootItems(context);
        Demo.insertDemo(context);
        setDefaultUserSettings(context);
        settings.setLucyIsInitialized(true, context);
        settings.reload(context);
    }
    public static void setDefaultUserSettings(Context context){
        log("Lucinda.setDefaultUserSettings()");
        UserPrefs.setPanicUrls(context.getResources().getStringArray(R.array.panicUrls), context);
        UserPrefs.setCategories(context.getResources().getStringArray(R.array.categories), context);
        UserPrefs.setUsesPassword(false, context);
        UserPrefs.setUseDarkMode(true, context);

    }
}
