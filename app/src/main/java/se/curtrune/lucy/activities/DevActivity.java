package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.economy.EconomyActivity;
import se.curtrune.lucy.activities.economy.persist.ECDBAdmin;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.dialogs.RepeatDialog;
import se.curtrune.lucy.persist.DBAdmin;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;
import se.curtrune.lucy.util.Logger;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.NotificationsWorker;
import se.curtrune.lucy.workers.SettingsWorker;

public class DevActivity extends AppCompatActivity {
    private TextView textViewSwipeAble;
    private TextView textViewNewMain;
    private TextView textViewVersionName;
    private TextView textViewLucindaVersion;
    private  TextView textViewAndroidVersion;
    private TextView textViewLanguage;
    private TextView textViewFirstInstalled;
    private TextView textViewUpdated;
    private TextView textViewModel;


    private Lucinda lucinda;
    public static boolean VERBOSE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_activity);
        initCatchAllExceptionsHandler();
        setTitle("lucinda");
        log("DevActivity.onCreate(Bundle)");
        printSystemInfo();
        lucinda = Lucinda.getInstance(this);
        if (!lucinda.isInitialized(this)) {
            log("...lucinda not initialized");
            try {
                lucinda.initialize(this);
            } catch (SQLException e) {
                Toast.makeText(this, "serious, failure to initialize the app", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            log("Lucinda is initialized!");
        }
        initComponents();
        initListeners();
        checkNotificationPermission();
        setUserInterface();
        NotificationsWorker.createNotificationChannel(this);
        openDB();
    }
    private void initCatchAllExceptionsHandler(){
        Thread.setDefaultUncaughtExceptionHandler((paramThread, paramThrowable) -> {

            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(DevActivity.this,paramThrowable.getMessage(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
            try
            {
                Thread.sleep(4000); // Let the Toast display before app will get shutdown
            }
            catch (InterruptedException e) {    }
            System.exit(2);
        });
    }

    private void checkNotificationPermission() {
        log("...checkNotificationPermission()");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            log("PERMISSION_GRANTED");
        } else {
            log("PERMISSION_DENIED");
        }
    }

    private void createEconomyTables() {
        log("...createEconomyTables()");
        ECDBAdmin.createEconomyTables(this);
        Toast.makeText(this, "tables created, possibly", Toast.LENGTH_LONG).show();
        DBAdmin.listTables(this);
    }
    private void createLoggerTable(){
        log("...createLoggerTable()");
        String queery = Queeries.CREATE_TABLE_LOGGER;

    }

    private void initComponents() {
        if( VERBOSE) log("...initComponents()");

        textViewNewMain = findViewById(R.id.devActivity_mainActivity);
        textViewLucindaVersion = findViewById(R.id.devActivity_lucindaVersionCode);
        textViewAndroidVersion = findViewById(R.id.devActivity_androidVersion);
        textViewSwipeAble = findViewById(R.id.devActivity_economy);
        textViewLanguage = findViewById(R.id.devActivity_language);
        textViewFirstInstalled = findViewById(R.id.devActivity_firstInstalled);
        textViewVersionName = findViewById(R.id.devActivity_lucindaVersionName);
        textViewModel = findViewById(R.id.devActivity_model);
        textViewUpdated = findViewById(R.id.devActivity_updated);
    }

    private void initListeners() {
        if( VERBOSE) log("...initListeners()");
        textViewSwipeAble.setOnClickListener(view -> startActivity(new Intent(this, EconomyActivity.class)));
        textViewNewMain.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
    }
    private void printSystemInfo(){
        log("...printSystemInfo()");
        log("\tSDK_INT", Build.VERSION.SDK_INT);
        log("\tDEVICE", Build.DEVICE);
        log("\tUSER", Build.USER);
        log("\tHARDWARE", Build.HARDWARE);
        log("\tBRAND", Build.BRAND);
        log("\tMANUFACTURER", Build.MANUFACTURER);
        log("\tMODEL", Build.MODEL);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            log("...versionName", pInfo.versionName);
            log("...versionCode", pInfo.versionCode);
            log("...packageName", pInfo.packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void listTables() {
        log("...listTables()");
        try(LocalDB db = new LocalDB(this)) {
            List<String> tables = db.getTableNames();
            tables.forEach(Logger::log);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dev_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.homeActivity_openDB) {
            openDB();
        } else if (item.getItemId() == R.id.homeActivity_createTableItems) {
            createItemsTable();
        } else if (item.getItemId() == R.id.homeActivity_dropTableItems) {
            deleteItemsTable();
        } else if (item.getItemId() == R.id.homeActivity_createTableMental) {
            createTableMental();
        } else if (item.getItemId() == R.id.homeActivity_listTables) {
            listTables();
        } else if (item.getItemId() == R.id.homeActivity_dropTableMental) {
            dropTableMental();
        } else if (item.getItemId() == R.id.homeActivity_resetApp) {
            resetApp();
        } else if (item.getItemId() == R.id.homeActivity_createTableCategories) {
            createTableCategories();
        } else if (item.getItemId() == R.id.homeActivity_createEconomyTables) {
            createEconomyTables();
        }else if (item.getItemId() == R.id.homeActivity_setNotifications){
            testNotification();
        }else if( item.getItemId() == R.id.homeActivity_logInActivity){
            startActivity(new Intent(this, LogInActivity.class));
        }else if ( item.getItemId() == R.id.devActivity_userSettings){
            setDefaultUserSettings();
        }else if( item.getItemId() == R.id.devActivity_clearSettings){
            Settings.removeAll(this);
        }else if( item.getItemId() == R.id.devActivity_repeatDialog){
            showRepeatDialog();
        }
        return true;
    }

    private void openDB() {
        log("...openDB");
        try (LocalDB db = new LocalDB(this)) {
            db.open();
        }catch (Exception e){
            log(e.getMessage());
        }
    }

    private void deleteItemsTable() {
        log("...deleteItemsTable()");
        try(LocalDB db = new LocalDB(this)) {
            db.executeSQL(Queeries.DROP_TABLE_ITEMS);
        }
    }

    private void createItemsTable() {
        log("...createItemsTable()");
        try(LocalDB db = new LocalDB(this)) {
            db.executeSQL(Queeries.CREATE_TABLE_ITEMS);
        }
    }

    private void createTableMental() {
        log("...createTableMental()");
        try(LocalDB db = new LocalDB(this)) {
            db.executeSQL(Queeries.CREATE_TABLE_MENTAL);
        }
    }

    private void dropTableMental() {
        log("...dropTableMental()");
        try(LocalDB db = new LocalDB(this)){
            db.executeSQL(Queeries.DROP_TABLE_MENTAL);
        }
    }

    private void createTableCategories() {
        log("...createTableCategories()");
        try(LocalDB db = new LocalDB(this)) {
            db.executeSQL(Queeries.CREATE_TABLE_CATEGORIES);
            populateCategories();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void populateCategories() {
        log("...populateCategories()");
        try(LocalDB db = new LocalDB(this)) {
            for (String category : Lucinda.CATEGORIES) {
                db.executeSQL(Queeries.insertCategory(category));
            }
        }
    }

    private void resetApp() {
        log("...resetApp()");
        try {
            lucinda.reset(this);
            Toast.makeText(this, "lucinda reset", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void setDefaultUserSettings(){
        log("...setDefaultUserSettings");
        Lucinda.setDefaultUserSettings(this);
    }
    private void setNotifications(){
        log("...setNotifications()");
        NotificationsWorker.setNotifications(LocalDate.now(), this);
    }
    private void setUserInterface(){
        log("...setUserInterface()");
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String stringVersionCode = String.format(Locale.getDefault(), "VERSION_CODE: %d", pInfo.versionCode );
            textViewLucindaVersion.setText(stringVersionCode);
            String stringVersionName = String.format(Locale.getDefault(), "VERSION NAME: %s", pInfo.versionName);
            textViewVersionName.setText(stringVersionName);
            String stringAndroidVersion = String.format(Locale.getDefault(), "SDK_INT: %d",Build.VERSION.SDK_INT );
            textViewAndroidVersion.setText(stringAndroidVersion);
            String stringLanguage = String.format(Locale.getDefault(), "LANGUAGE: %s", SettingsWorker.getLanguage());
            textViewLanguage.setText(stringLanguage);

            long firstInstallTime = pInfo.firstInstallTime;
            LocalDateTime installTime = LocalDateTime.ofEpochSecond(firstInstallTime /1000 , 0, ZoneOffset.UTC);
            String stringFirstInstalled = String.format(Locale.getDefault(), "FIRST INSTALLED: %s", installTime.toString());
            textViewFirstInstalled.setText(stringFirstInstalled);

            LocalDateTime updated = LocalDateTime.ofEpochSecond(pInfo.lastUpdateTime /1000, 0, ZoneOffset.UTC);
            String stringUpdated = String.format(Locale.getDefault(), "UPDATED: %s",updated.toString());
            textViewUpdated.setText(stringUpdated);

            String stringModel = String.format(Locale.getDefault(), "MODEL: %s",Build.MODEL);
            textViewModel.setText(stringModel);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void showRepeatDialog(){
        log("...showRepeatDialog()");
        RepeatDialog dialog = new RepeatDialog();
        dialog.setCallback(new RepeatDialog.Callback() {
            @Override
            public void onRepeat(Repeat.Period period) {
                log("...onRepeat(Period)", period.toString());
            }
        });
        dialog.show(getSupportFragmentManager(), "repeat dialog");

    }
    private void testNotification(){
        log("...testNotification()");
        Notification notification = new Notification();
        LocalTime targetTime = LocalTime.now().plusMinutes(5);
        notification.setTime(targetTime);
        notification.setDate(LocalDate.now());
        Item item = new Item();
        String heading = String.format("notify me %s", notification.getTime().toString());
        item.setHeading(heading);
        item.setTargetDate(LocalDate.now());
        item.setTargetTime(targetTime);
        Item todoParent = ItemsWorker.getRootItem(Settings.Root.TODO, this);
        item.setParentId(todoParent.getID());
        item = ItemsWorker.insertChild(todoParent, item, this);
        item.setNotification(notification);
        log(item);
        NotificationsWorker.setNotification(item, this);
    }
    private void toggleDarkMode() {
        log("...toggleDarkMode()");
        UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        //uiModeManager.getCurrentModeType();
        int currentMode = uiModeManager.getNightMode();
        log("...currentMode");
        switch (currentMode) {
            case UiModeManager.MODE_NIGHT_NO:
                log("MODE_NIGHT_NO");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case UiModeManager.MODE_NIGHT_YES:
                log("MODE_NIGHT_YES");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case UiModeManager.MODE_NIGHT_AUTO:
                log("MODE_NIGHT_AUTO");
                break;
            case UiModeManager.MODE_NIGHT_CUSTOM:
                log("MODE_NIGHT_CUSTOM");
                break;
        }
    }
}
