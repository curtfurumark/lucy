package se.curtrune.lucy.app;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.content.SharedPreferences;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Type;

public class Settings {
    //root ids
    private static final String PREFERENCES_NAME =  "PREFERENCES_NAME";
    private static final String IS_INITIALIZED = "IS_INITIALIZED";
    private long todoID;
    private long dailyID;
    private long projectsID;
    private long appointmentsID;
    private long panicID;
    private long theRootID;
    private boolean isInitialized = false;
    private SharedPreferences.Editor editor;
    private static Settings instance;



    public void reload(Context context) {
        log("...reload(Context)");
        init(context);
    }

    public enum Root{
        TODO, DAILY, PROJECTS, APPOINTMENTS, PANIC, THE_ROOT
    }
    private Settings(Context context){
        init(context);
    }
    public static Settings getInstance(Context context){
        if( instance == null){
            instance = new Settings(context);
        }
        return instance;
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

    public static Item getAppointmentsRoot() {
        Item item = new Item("appointments");
        item.setType(Type.ROOT);
        return item;
    }

    /**
     * these are the default categories, to be added or subtracted from
     * @return, an array containing the default categories
     */
    public static String[] getCategories() {
        return new String[]{"household", "work", "leisure", "read", "health"};
    }
    public static Item getPanicRoot(){
        Item item  = new Item("panic");
        item.setType(Type.ROOT);
        return item;
    }

    public static Item getTodayRoot() {
        Item item = new Item("today");
        item.setType(Type.ROOT);
        return item;
    }

    public static Item getTodoRoot() {
        Item item = new Item("todo");
        item.setType(Type.ROOT);
        return item;
    }

    public static Item getProjectsRoot() {
        Item item = new Item("projects");
        item.setType(Type.ROOT);
        return item;
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
    public void setLucyIsInitialized(boolean isInitialized, Context context) {
            log("Lucinda.setAppInitialized(boolean)", isInitialized);
            editor = getEditor(context);
            editor.putBoolean(IS_INITIALIZED, isInitialized);
            editor.commit();
    }
}
