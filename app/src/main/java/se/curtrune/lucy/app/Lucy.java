package se.curtrune.lucy.app;

import se.curtrune.lucy.R;
import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.sql.SQLException;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.enums.ViewMode;
import se.curtrune.lucy.persist.DBAdmin;

public class Lucy {

    private static Lucy instance;
    private final Settings settings;
    public static Item currentParent;
    public static ViewMode currentViewMode;

/*    public static final int MOOD_OFFSET = 5;
    public static final int ENERGY_OFFSET = 5;*/
    public static String[] CATEGORIES = new String[] {"household", "work", "health", "play"};

    private Lucy(Context context){
        log("...Lucy(Context)");
        settings = Settings.getInstance(context);
    }
    public static Lucy getInstance(Context context){
        if( instance == null){
            instance = new Lucy(context);
        }
        return instance;
    }
    public  void initialize(Context context) throws SQLException {
        log("Lucy.initialize(Context)");
        initTheApp(context);
        settings.setLucyIsInitialized(true, context);
        log("...lucy is initialized");
    }


    public boolean isInitialized(Context context){
        log("Settings.isInitialized(Context)");
        return settings.isInitialized(context);
    }
    public void initTheApp(Context context) throws SQLException {
        log("Lucy.initTheApp(Context)");
        DBAdmin.createTables(context);
        DBAdmin.insertCategories(context);
        DBAdmin.insertRootItems(context);
    }

    public static int getItemID(ViewMode mode){
        log("Lucy.getItemID(ViewMode)", mode.toString());
        if( mode.equals(ViewMode.TODO)){
            return R.id.bottomNavigation_todo;
        }else if (mode.equals(ViewMode.TODAY)){
            return R.id.bottomNavigation_today;
        }else if (mode.equals(ViewMode.PROJECTS)){
            return R.id.bottomNavigation_projects;
        }else if (mode.equals(ViewMode.ENCHILADA)){
            return R.id.bottomNavigation_enchilada;
        }
        return R.id.bottomNavigation_todo;
    }
    public void reset(Context context) throws SQLException {
        log("Lucy.reset(Context)");
        DBAdmin.dropTables(context);
        DBAdmin.createTables(context);
        DBAdmin.insertCategories(context);
        DBAdmin.insertRootItems(context);
        Demo.insertDemo(context);
        log("..almost done, possibly maybe");
        settings.setLucyIsInitialized(true, context);
        settings.reload(context);
    }

    public void setIsInitialized(boolean b, Context context) {
        log("Lucy.setIsInitialized(boolean, Context");
        settings.setLucyIsInitialized(b, context);
    }
}
