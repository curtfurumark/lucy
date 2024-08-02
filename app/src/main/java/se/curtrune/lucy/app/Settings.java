package se.curtrune.lucy.app;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Type;

public class Settings {
    private static final String PREFERENCES_NAME =  "PREFERENCES_NAME";
    private static final String IS_INITIALIZED = "IS_INITIALIZED";
    public static final String DEV_EMAIL = "curt.furumark@gmail.com";

    public static final String USER = "USER";
    //public static final String PWD = "PWD";
    private long todoID;
    private long dailyID;
    private long projectsID;
    private long appointmentsID;
    private long panicID;
    private long theRootID;
    private boolean isInitialized = false;



    public enum PanicAction{
        GAME, URL, SEQUENCE, PENDING, ICE
    }
    public enum Root{
        TODO, DAILY, PROJECTS, APPOINTMENTS, PANIC, THE_ROOT
    }
    private SharedPreferences.Editor editor;
    private static Settings instance;
    private Settings(Context context){
        init(context);
    }

    public static void addString(String key, String value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }
    public static void addInt(String key, int value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void addBoolean(String key, boolean value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static int getInt(String key, int defaultValue, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key , defaultValue);
    }

    public static Set<String> getSet(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return new HashSet<>(sharedPreferences.getStringSet(key , new HashSet<>()));
    }
    public static List<String> getList(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return new ArrayList<>(sharedPreferences.getStringSet(key , new ArraySet<>()));
    }
    public void reload(Context context) {
        log("...reload(Context)");
        init(context);
    }



    public static Settings getInstance(Context context){
        if( instance == null){
            instance = new Settings(context);
        }
        return instance;
    }
    public static void removeAll(Context context){
        log("Settings.removeAll(Context)");
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

    /**
     * reads values from sharedpreferences  and initializes the Settings instance using those values
     * @param context, forever context
     */
    private void init(Context context){
        log("...init(Context)");
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        todoID = sharedPreferences.getLong(Root.TODO.toString(), -1);
        dailyID = sharedPreferences.getLong(Root.DAILY.toString(), -1);
        projectsID = sharedPreferences.getLong(Root.PROJECTS.toString(), -1);
        appointmentsID = sharedPreferences.getLong(Root.APPOINTMENTS.toString(), -1);
        panicID = sharedPreferences.getLong(Root.PANIC.toString(), -1);
        theRootID = sharedPreferences.getLong(Root.THE_ROOT.toString(), 1);
    }

    private void addToPrefs(String key, String value, Context context){
        log("Settings.addToPrefs(String, String)");
        editor.putString(key, value);
        editor.commit();
    }
    private void addToPrefs(String key, long  value, Context context){
        log("Settings.addToPrefs(String, long)");
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }
    public boolean isInitialized(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_INITIALIZED,  false);
    }
    public static boolean getBoolean(String key, boolean defaultValue, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,  defaultValue);
    }


    public static String getString(String key, String defaultValue, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    public long getRootID(Root root){
        switch (root){
            case DAILY:
                return dailyID;
            case PROJECTS:
                return projectsID;
            case APPOINTMENTS:
                return appointmentsID;
            case THE_ROOT:
                return theRootID;
            case PANIC:
                return panicID;
            default:
                return todoID;
        }
    }

    public void addRootID(Settings.Root root, long id, Context context){
        addToPrefs(root.toString(), id, context);
    }

    private SharedPreferences.Editor getEditor(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.edit();
    }
    public static void printAll(Context context){
        log("Settings.printAll()");
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Map<String, ?> prefMap = sharedPreferences.getAll();
        log("...prefMap size", prefMap.size());
        for( Map.Entry<String, ?> entry: prefMap.entrySet()){
            log("...an entry set");
            System.out.printf("%s: %s\n", entry.getKey(),entry.getValue().toString());
        }
    }
    public static void removeEntry(String key, Context context){
        SharedPreferences preferences = context.getSharedPreferences("Mypref", 0);
        preferences.edit().remove(key).commit();
    }
    public static void saveList(String key, List<String> list, Context context) {
        log("Settings.saveList(String, List<String>, Context)");
        Set<String> stringSet = new HashSet<>(list);
        saveSet(key, stringSet, context);
    }
    public static void saveSet(String key,Set<String> list,  Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, list);
        editor.commit();
    }
    public void setLucyIsInitialized(boolean isInitialized, Context context) {
            log("Lucinda.setAppInitialized(boolean)", isInitialized);
            editor = getEditor(context);
            editor.putBoolean(IS_INITIALIZED, isInitialized);
            editor.commit();
    }
}
