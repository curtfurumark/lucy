package se.curtrune.lucy.app;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.sql.SQLException;

import se.curtrune.lucy.persist.DBAdmin;
import se.curtrune.lucy.persist.SettingsStore;

public class Lucinda {

    private static Lucinda instance;
    private final SettingsStore settings;
    //public static Item currentParent;
    public static boolean VERBOSE = false;
    public static boolean Dev = false;
    private static final String KEY_NIGHTLY_ALARM ="KEY_NIGHTLY_ALARM";

    private Lucinda(Context context){
        if( VERBOSE) log("Lucinda(Context)");
        settings = SettingsStore.getInstance(context);
    }
    public static Lucinda getInstance(Context context){
        if( instance == null){
            instance = new Lucinda(context);
        }
        return instance;
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
        settings.setLucyIsInitialized(true, context);
    }
    public static boolean nightlyAlarmIsSet(Context context) {
        log("Lucinda.nightlyAlarmIsSet(Context)");
        return SettingsStore.getBoolean(KEY_NIGHTLY_ALARM, false, context);
    }

    public void reset(Context context) throws SQLException {
        log("Lucinda.reset(Context)");
        DBAdmin.dropTables(context);
        DBAdmin.createTables(context);
        DBAdmin.insertRootItems(context);
        //setDefaultUserSettings(context);
        settings.setLucyIsInitialized(true, context);
        settings.reload(context);
    }

}
