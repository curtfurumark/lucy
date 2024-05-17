package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.sql.SQLException;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.economy.EconomyActivity;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.classes.Quotes;
import se.curtrune.lucy.persist.DBAdmin;
import se.curtrune.lucy.activities.economy.persist.ECDBAdmin;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;
import se.curtrune.lucy.util.Logger;

public class HomeActivity extends AppCompatActivity {
    private TextView textViewSettings;
    private TextView textViewSwipeAble;
    private TextView textViewNewMain;

    private TextView textViewQuote;

    private Lucinda lucinda;
    public static boolean VERBOSE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        setTitle("lucinda");
        log("HomeActivity.onCreate(Bundle)");
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
        openDB();
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

    private void initComponents() {
        if( VERBOSE) log("...initComponents()");

        textViewQuote = findViewById(R.id.homeActivity_quote);
        textViewQuote.setSelected(true);
        textViewNewMain = findViewById(R.id.homeActivity_mainActivity);
        textViewSettings = findViewById(R.id.homeActivity_settings);
        textViewSwipeAble = findViewById(R.id.homeActivity_economy);
    }

    private void initListeners() {
        if( VERBOSE) log("...initListeners()");
        textViewSwipeAble.setOnClickListener(view -> startActivity(new Intent(this, EconomyActivity.class)));
        textViewNewMain.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
        textViewQuote.setOnClickListener(view -> randomQuote());
        textViewSettings.setOnClickListener(view -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void randomQuote() {
        log("...randomQuote()");
        String quote = Quotes.getRandomQuote(this);
        textViewQuote.setText(quote);
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
        getMenuInflater().inflate(R.menu.home_activity, menu);
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
            // toggleDarkMode();
            createEconomyTables();
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
}
