package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import se.curtrune.lucy.app.User;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Media;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.persist.DBAdmin;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;
import se.curtrune.lucy.util.Logger;
import se.curtrune.lucy.viewmodel.UpdateLucindaViewModel;
import se.curtrune.lucy.web.VersionInfo;
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
    private EditText editTextSql;
    private Button buttonRunSQL;
    private Button buttonRunCode;
    private CheckBox checkBoxDev;


    private Lucinda lucinda;
    public static boolean VERBOSE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_activity);
        setTitle("lucinda");
        log("DevActivity.onCreate(Bundle)");
        printSystemInfo();
        lucinda = Lucinda.getInstance(this);
        initComponents();
        initListeners();
        //addMentalToItemTable();
        setUserInterface();
        checkForLucindaUpdate();
        //openDB();
    }


    private void addMentalToItemTable(){
        log("...addMentalToItemTable()");
        String queeryEnergy = "ALTER TABLE items ADD COLUMN energy INTEGER DEFAULT 0";
        String queeryAnxiety = "ALTER TABLE items ADD COLUMN anxiety INTEGER DEFAULT 0";
        String queeryStress = "ALTER TABLE items ADD COLUMN stress INTEGER DEFAULT 0";
        String queeryMood = "ALTER TABLE items ADD COLUMN mood INTEGER DEFAULT 0";
        try(LocalDB db = new LocalDB(this)){
            //db.executeSQL(queeryEnergy);
            db.executeSQL(queeryAnxiety);
            db.executeSQL(queeryStress);
            db.executeSQL(queeryMood);
            log(" energy column created?");
        }catch (Exception e){
            log("an exception occurred");
            e.printStackTrace();
        }
    }
    private void clearShowInCalendar(){
        log("...clearShowInCalendar()");
        String queery = "UPDATE items set isCalenderItem = 0";
        try(LocalDB db = new LocalDB(this)){
            db.executeSQL(queery);
            Toast.makeText(this, "items updated", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void checkForLucindaUpdate(){
        log("...checkForLucindaUpdate()");
        UpdateLucindaViewModel updateLucindaViewModel = new ViewModelProvider(this).get(UpdateLucindaViewModel.class);
        updateLucindaViewModel.checkForNewVersion();
        updateLucindaViewModel.getVersionInfo().observe(this, new Observer<VersionInfo>() {
            @Override
            public void onChanged(VersionInfo versionInfo) {
                log("...onChanged, VersionInfo available", versionInfo.toString());
            }
        });
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
        try(LocalDB db = new LocalDB(this)){
            log("...queery", queery);
            db.executeSQL(queery);
        }
    }
    private void executeSQL(){
        log("...executeSQL()");
        addMentalToItemTable();
/*        String queery = editTextSql.getText().toString();
        if( queery.isEmpty()){
            Toast.makeText(this, "a sql statement pleast", Toast.LENGTH_LONG).setMentalType();
        }*/

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
        checkBoxDev = findViewById(R.id.devActivity_checkBoxDev);
        buttonRunSQL = findViewById(R.id.devActivity_buttonRunSQL);
        editTextSql = findViewById(R.id.devActivity_sql);
        buttonRunCode = findViewById(R.id.devActivity_buttonRunCode);
    }

    private void initListeners() {
        if( VERBOSE) log("...initListeners()");
        textViewSwipeAble.setOnClickListener(view -> startActivity(new Intent(this, EconomyActivity.class)));
        textViewNewMain.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
        checkBoxDev.setOnCheckedChangeListener((buttonView, isChecked) -> {
            log("...onCheckChanged(CompoundButton, boolean isChecked)", isChecked);
            User.setDevMode(isChecked,this );
            Lucinda.Dev = isChecked;
        });
        buttonRunSQL.setOnClickListener(view->executeSQL());
        buttonRunCode.setOnClickListener(view->runCode());
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
            //String version = pInfo.versionName;
            log("...versionName", pInfo.versionName);
            log("...versionCode", pInfo.versionCode);
            log("...packageName", pInfo.packageName);
            ApplicationInfo applicationInfo = pInfo.applicationInfo;
            log("...dataDir", applicationInfo.dataDir);
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
        } else if (item.getItemId() == R.id.homeActivity_createEconomyTables) {
            createEconomyTables();
        }else if (item.getItemId() == R.id.devActivity_testNotification){
            testNotification();
        }else if( item.getItemId() == R.id.homeActivity_logInActivity){
            startActivity(new Intent(this, LogInActivity.class));
        }else if ( item.getItemId() == R.id.devActivity_userSettings){
            setDefaultUserSettings();
        }else if( item.getItemId() == R.id.devActivity_clearSettings){
            //Settings.removeAll(this);
            Toast.makeText(this, "don't do this", Toast.LENGTH_LONG).show();
        }else if( item.getItemId() == R.id.devActivity_repeatDialog){
            //showRepeatDialog();
            Toast.makeText(this, "for future use", Toast.LENGTH_LONG).show();
        }else if( item.getItemId() == R.id.homeActivity_setNotifications){
            setNotifications();
        }
        return true;
    }

    private void openDB() {
        log("...openDB");
        LocalDB db = new LocalDB(this);
        db.open();
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
    private void createFamilyItems(){
        log("...createFamilyItems()");
        Item rootItem = new Item("rootItem");
        rootItem = ItemsWorker.insertChild(ItemsWorker.getDailyRoot(this), rootItem, this);
        Item child1 = new Item("child1");
        child1 = ItemsWorker.insertChild(rootItem, child1, this);
        Item child2 = new Item("child2");
        child2 = ItemsWorker.insertChild(rootItem, child2, this);
        Item grandchild1 = new Item("grandChild1");
        grandchild1 = ItemsWorker.insertChild(child1, grandchild1, this);
        Item grandChild2 = new Item("grandChild2");
        grandChild2 = ItemsWorker.insertChild(child1, grandChild2, this);
        log("rootItem id", rootItem.getID());
    }
    private void deleteTree(Item parent){
        log("...deleteTree(Item)", parent.getHeading());
        ItemsWorker.deleteTree(parent, this);
    }
    private void creteItemWithMedia(){
        log("...createItemWithMedia()");
        Item item = new Item("item with media");
        Media media = new Media();
        media.setFileType(Media.FileType.TEXT);
        media.setFilePath("dkjdkj");
        item.setContent(media);
        item = ItemsWorker.insertChild(ItemsWorker.getRootItem(Settings.Root.PROJECTS, this), item, this);
        log("...item inserted with id", item.getID());
    }
    private void runCode(){
        log("...runCode()");
        //Item item = ItemsWorker.selectItem(4244, this);
        //log("...item", item.getHeading());
        //deleteTree(item);
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
            //checkBoxDev.setChecked(User);
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
            checkBoxDev.setChecked(User.isDevMode(this));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
/*    private void showRepeatDialog(){
        log("...showRepeatDialog()");
        RepeatDialog dialog = new RepeatDialog();
        dialog.setCallback(new RepeatDialog.Callback() {
            @Override
            public void onRepeat(Repeat.Unit period) {
                log("...onRepeat(Unit)", period.toString());
            }
        });
        dialog.setMentalType(getSupportFragmentManager(), "repeat dialog");

    }*/
    private void testNotification(){
        log("...testNotification()");
        Notification notification = new Notification();
        LocalTime targetTime = LocalTime.now().plusMinutes(5);
        notification.setTime(targetTime);
        notification.setDate(LocalDate.now());
        Item item = new Item();
        String heading = String.format(Locale.getDefault(), "notify me %s", notification.getTime().toString());
        item.setHeading(heading);
        item.setTargetDate(LocalDate.now());
        item.setTargetTime(targetTime);
        Item todoParent = ItemsWorker.getRootItem(Settings.Root.TODO, this);
        item.setParentId(todoParent.getID());
        item.setNotification(notification);
        item = ItemsWorker.insertChild(todoParent, item, this);
        log(item);
        NotificationsWorker.setNotification(item, this);
    }
}
