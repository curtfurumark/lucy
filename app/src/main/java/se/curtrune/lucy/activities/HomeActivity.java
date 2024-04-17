package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.os.LocaleListCompat;

import java.sql.SQLException;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.economy.EconomyActivity;
import se.curtrune.lucy.activities.economy.TransactionActivity;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.classes.Quotes;
import se.curtrune.lucy.persist.DBAdmin;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;
import se.curtrune.lucy.util.Logger;
import se.curtrune.lucy.workers.WebWorker;

public class HomeActivity extends AppCompatActivity {
    private TextView textViewToday;
    private TextView textViewSettings;
    private TextView textViewSwipeAble;
    private TextView textViewNewMain;
    private TextView textViewCalender;
    private TextView textViewStatistics;
    private TextView textViewQuote;
    private Lucinda lucinda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        setTitle("lucinda");
        //changeLanguage();
        log("HomeActivity.onCreate(Bundle)");
        lucinda = Lucinda.getInstance(this);
        //lucinda.setIsInitialized(true, this);
        if( !lucinda.isInitialized(this)){
            log("...lucinda not initialized");
            try {
                lucinda.initialize(this);
            } catch (SQLException e) {
                Toast.makeText(this, "serious, failure to initialize the app", Toast.LENGTH_LONG).show();
                return;
            }
        }else{
            log("Lucinda is initialized!");
        }
        initComponents();
        initListeners();
        //randomQuote();
        //randomAffirmation();
        checkNotificationPermission();
        openDB();
    }
    private void checkNotificationPermission(){
        log("...checkNotificationPermission()");
        if( ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            log("PERMISSION_GRANTED");
        }else{
            log("PERMISSION_DENIED");
        }

    }

    private void initComponents(){
        log("...initComponents()");
        textViewToday = findViewById(R.id.homeActivity_today);
        textViewStatistics = findViewById(R.id.homeActivity_statistics);
        textViewQuote = findViewById(R.id.homeActivity_quote);
        textViewQuote.setSelected(true);
        textViewCalender = findViewById(R.id.homeActivity_calender);
        textViewNewMain = findViewById(R.id.homeActivity_mainActivity);
        textViewSettings = findViewById(R.id.homeActivity_settings);
        textViewSwipeAble = findViewById(R.id.homeActivity_economy);
    }
    private void initListeners(){
        log("...initListeners()");
        textViewSwipeAble.setOnClickListener(view->startActivity(new Intent(this,  EconomyActivity.class)));
        textViewToday.setOnClickListener(view -> startActivity(new Intent(this, TodayActivity.class)));
        textViewStatistics.setOnClickListener(view->startActivity(new Intent(this, StatisticsMain.class)));
        textViewNewMain.setOnClickListener(view->startActivity(new Intent(this, MainActivity.class)));
        textViewCalender.setOnClickListener(view->startActivity(new Intent(this, MyCalenderActivity.class)));
        textViewQuote.setOnClickListener(view->randomQuote());
        textViewSettings.setOnClickListener(view->startActivity(new Intent(this, SettingsActivity.class)));
    }
    private void randomQuote(){
        log("...randomQuote()");
        String quote = Quotes.getRandomQuote(this);
        textViewQuote.setText(quote);

    }
    private void listTables(){
        log("...listTables()");
        LocalDB db = new LocalDB(this);
        List<String> tables = db.getTableNames();
        tables.forEach(Logger::log);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.homeActivity_openDB){
            openDB();
        }else if( item.getItemId() == R.id.homeActivity_createTableItems){
            createItemsTable();
        }else if( item.getItemId() == R.id.homeActivity_dropTableItems){
            deleteItemsTable();
        }else if( item.getItemId() == R.id.homeActivity_createTableMental){
            createTableMental();
        }else if( item.getItemId() == R.id.homeActivity_listTables){
            listTables();
        }else if( item.getItemId() == R.id.homeActivity_dropTableMental){
            dropTableMental();
        }else if( item.getItemId() == R.id.homeActivity_resetApp){
            resetApp();
        }else if( item.getItemId() == R.id.homeActivity_createTableCategories){
            createTableCategories();
        }else if( item.getItemId() == R.id.homeActivity_toggleDarkMode){
            toggleDarkMode();
        }
        return true;
    }

    private void openDB(){
        log("...openDB");
        LocalDB db = new LocalDB(this);
        db.open();
    }
    private void deleteItemsTable(){
        log("...deleteItemsTable()");
        LocalDB db = new LocalDB(this);
        db.executeSQL(Queeries.DROP_TABLE_ITEMS);
    }

    private void changeLanguage(){
        log("...changeLanguage()");
/*        Locale locale = new Locale("en GB");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());*/


        LocaleListCompat  localeListCompat = LocaleListCompat.forLanguageTags("en");
        AppCompatDelegate.setApplicationLocales(localeListCompat);
    }
    private void createItemsTable(){
        log("...createItemsTable()");
        LocalDB db = new LocalDB(this);
        db.executeSQL(Queeries.CREATE_TABLE_ITEMS);
    }
    private void createTableMental(){
        log("...createTableMental()");
        LocalDB db = new LocalDB(this);
        db.executeSQL(Queeries.CREATE_TABLE_MENTAL);
    }
    private void dropTableMental(){
        log("...dropTableMental()");
        LocalDB db = new LocalDB(this);
        db.executeSQL(Queeries.DROP_TABLE_MENTAL);
    }
    private void createTableCategories(){
        log("...createTableCategories()");
        LocalDB db = new LocalDB(this);
        db.executeSQL(Queeries.CREATE_TABLE_CATEGORIES);
        populateCategories();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //changeLanguage();
    }

    private void populateCategories(){
        log("...populateCategories()");
        LocalDB db = new LocalDB(this);
        for(String category : Lucinda.CATEGORIES){
            db.executeSQL(Queeries.insertCategory(category));
        }
    }
    private void randomAffirmation(){
        log("...randomAffirmation()");
        WebWorker.requestAffirmation(affirmation -> {
            textViewQuote.setText(affirmation.getAffirmation());
/*            new Handler().postDelayed(() -> {
                log("...run()");
                startActivity(new Intent(this, TodayActivity.class));

            },2000);*/
        });

    }
    private void resetApp(){
        log("...resetApp()");
        try {
            lucinda.reset(this);
            Toast.makeText(this, "lucinda reset", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void testCategories(){
        log("...testCategories()");
        DBAdmin.insertCategories(this);
    }
    private void toggleDarkMode(){
        log("...toggleDarkMode()");
        UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        //uiModeManager.getCurrentModeType();
        int currentMode = uiModeManager.getNightMode();
        log("...currentMode");
        switch (currentMode){
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
/*        int currentNightMode = Configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                break;*/
    }
}