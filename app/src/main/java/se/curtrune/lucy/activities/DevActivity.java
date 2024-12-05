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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.economy.EconomyActivity;
import se.curtrune.lucy.adapters.DevActivityAdapter;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.app.User;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Media;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.dev.ItemsTest;
import se.curtrune.lucy.dev.RepeatTest;
import se.curtrune.lucy.dev.UpdaterTest;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.dialogs.RepeatDialog;
import se.curtrune.lucy.persist.DBAdmin;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.viewmodel.DevActivityViewModel;
import se.curtrune.lucy.viewmodel.UpdateLucindaViewModel;
import se.curtrune.lucy.web.VersionInfo;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.NotificationsWorker;
import se.curtrune.lucy.workers.RepeatWorker;

public class DevActivity extends AppCompatActivity {
    private TextView textViewSwipeAble;
    private TextView textViewNewMain;
    private EditText editTextSql;
    private Button buttonRunSQL;
    private Button buttonRunCode;
    private CheckBox checkBoxDev;
    private DevActivityAdapter adapter;
    private DevActivityViewModel devActivityViewModel;
    private RecyclerView recyclerView;
    private boolean alarmRinging = false;

    private Lucinda lucinda;
    public static boolean VERBOSE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_activity);
        setTitle("lucinda");
        log("DevActivity.onCreate(Bundle)");
        printSystemInfo();
        initViewModel();
        lucinda = Lucinda.getInstance(this);
        initComponents();
        initListeners();
        initRecycler();
        //addMentalToItemTable();
        setUserInterface();
        //listTables();
        //listColumns();
        //openDB();
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



    private void executeSQL(){
        log("...executeSQL()");
        //addMentalToItemTable();
/*        String queery = editTextSql.getText().toString();
        if( queery.isEmpty()){
            Toast.makeText(this, "a sql statement pleast", Toast.LENGTH_LONG).setMentalType();
        }*/

    }

    private void initComponents() {
        if( VERBOSE) log("...initComponents()");
        textViewNewMain = findViewById(R.id.devActivity_mainActivity);
        textViewSwipeAble = findViewById(R.id.devActivity_economy);
        checkBoxDev = findViewById(R.id.devActivity_checkBoxDev);
        buttonRunSQL = findViewById(R.id.devActivity_buttonRunSQL);
        editTextSql = findViewById(R.id.devActivity_sql);
        buttonRunCode = findViewById(R.id.devActivity_buttonRunCode);
        recyclerView = findViewById(R.id.devActivity_recycler);
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
    private void initRecycler(){
        log("...initRecycler()");
        adapter = new DevActivityAdapter(devActivityViewModel.getLucindaInfo());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
    private void initViewModel(){
        log("...initViewModel()");
        devActivityViewModel = new ViewModelProvider(this).get(DevActivityViewModel.class);
        devActivityViewModel.init(this);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                log("...long versionCode", pInfo.getLongVersionCode());
            }
            log("...packageName", pInfo.packageName);
            long firstInstallTime = pInfo.firstInstallTime ;
            log("firstInstallTime", firstInstallTime);
            log("converted install time", Converter.epochToDate(firstInstallTime/1000));
            ApplicationInfo applicationInfo = pInfo.applicationInfo;
            log("...dataDir", applicationInfo.dataDir);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void listColumns(){
        log("...listColumns");
        devActivityViewModel.listColumns(this);
    }

    private void listTables() {
        log("...listTables()");
        DBAdmin.getTableNames(this).forEach(System.out::println);
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
            DBAdmin.createItemsTable(this);
        } else if (item.getItemId() == R.id.homeActivity_dropTableItems) {
            DBAdmin.dropTableItems(this);
        } else if (item.getItemId() == R.id.homeActivity_listTables) {
            listTables();
        } else if (item.getItemId() == R.id.homeActivity_dropTableMental) {
            DBAdmin.dropTableMental(this);
        } else if (item.getItemId() == R.id.homeActivity_resetApp) {
            resetApp();
        } else if (item.getItemId() == R.id.homeActivity_createEconomyTables) {
            DBAdmin.createEconomyTables(this);
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
            showRepeatDialog();
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


    private void resetApp() {
        log("...resetApp()");
        try {
            lucinda.reset(this);
            Toast.makeText(this, "lucinda reset", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
        //RepeatTest.createRepeatItemDecember(this);
        //RepeatTest.selectRepeats(this);
        //RepeatTest.listInstances(6, this);
        //RepeatTest.setLastDate(6, LocalDate.of(2024, 12, 22), this);
        //UpdaterTest.checkForUpdate(this);
        //RepeatTest.updateRepeat(this);
        RepeatTest.updateRepeats(this);
        //RepeatTest.repeatTest01(this);
        //RepeatTest.insertRepeatTest(this);
        //RepeatTest.selectRepeat(this);
        //RepeatTest.updateRepeat(this);//
        //RepeatTest.deleteRepeat();
        //RepeatTest.addColumnRepeatID(this);
        //RepeatTest.listColumns("items", this);
        //Item item = ItemsWorker.selectItem(4244, this);
        //log("...item", item.getHeading());
        //deleteTree(item)
/*        if( alarmRinging){
            AlarmTest.stopAlarm();
            alarmRinging = false;
            buttonRunCode.setText("start alarm");
        }else {
            alarmRinging = true;
            buttonRunCode.setText("stop alarm");
            AlarmTest.soundAlarm(this);
        }*/
        //RepeatTest.selectRepeats(this);
        //ItemsTest.testDeleteTree(this);
        //ItemsTest.createTree(this);
        //ItemsTest.deleteTree(4924, this);
/*        if( NotificationTest.notificationSet){
            log("will cancel alarm");
            NotificationTest.cancelAlarm(NotificationTest.notificationItem.getID(), this);
            Toast.makeText(this, "notification cancelled????", Toast.LENGTH_LONG).show();
        }else {
            LocalDateTime dateTime = LocalDateTime.now().plusMinutes(5);
            String heading = String.format(Locale.getDefault(), "notification %s", dateTime.toString());
            NotificationTest.setNotification(heading, "hello content", dateTime, this);
            buttonRunCode.setText("cancel alarm");
            Toast.makeText(this, "notification set", Toast.LENGTH_LONG).show();
        }*/
/*        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.SET_ALARM) == PackageManager.PERMISSION_GRANTED){
            log("...got myself permission to set alarm, yeah");
            AlarmTest.setAlarmUsingIntent(this, 16, 40);
        }else{
            Toast.makeText(this, "need to ask permission to set alarm", Toast.LENGTH_LONG).show();
        }*/

        //showAddItemDialog();
    }

    private void printTableNames(){
        log("...printTableNames()");
        try(LocalDB db = new LocalDB(this)){
            List<String> names = db.getTableNames();
            names.forEach(System.out::println);
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
        checkBoxDev.setChecked(User.isDevMode(this));
    }
    private void showAddItemDialog(){
        log("...showAddItemDialog()");
        Item parent = ItemsWorker.getRootItem(Settings.Root.TODO, this);
        AddItemDialog dialog = new AddItemDialog(parent, null);
        dialog.setCallback(new AddItemDialog.Callback() {
            @Override
            public void onAddItem(Item item) {
                log("...onAddItem(Item)", item.getHeading());
                //log(item);
                if( item.hasPeriod()){
                    log(item);
                    RepeatWorker.insertItemWithRepeat(item, getApplicationContext());
                }else{
                    log("Item without repeat, do nothing");
                }
            }
        });
        dialog.show(getSupportFragmentManager(), "addItem");
    }
    private void showRepeatDialog(){
        log("...showRepeatDialog()");
        RepeatDialog dialog = new RepeatDialog();
        dialog.setCallback(new RepeatDialog.Callback() {
            @Override
            public void onRepeat(Repeat repeat) {
                log("...onRepeat(Repeat)", repeat.toString());
                log(repeat);
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
