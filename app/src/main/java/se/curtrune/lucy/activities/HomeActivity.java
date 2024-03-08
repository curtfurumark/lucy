package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Quotes;
import se.curtrune.lucy.persist.DBAdmin;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;
import se.curtrune.lucy.util.Logger;
import se.curtrune.lucy.app.Lucy;

public class HomeActivity extends AppCompatActivity {
    private TextView textViewToday;
    //private TextView textViewProjects;
    private TextView textViewNewMain;
    private TextView textViewStatistics;
    private TextView textViewQuote;
    private Lucy lucy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        setTitle("lucinda");
        log("HomeActivity.onCreate(Bundle)");
        lucy = Lucy.getInstance(this);
        //lucy.setIsInitialized(true, this);
        if( !lucy.isInitialized(this)){
            log("...lucy not initialized");
            try {
                lucy.initialize(this);
            } catch (SQLException e) {
                Toast.makeText(this, "serious, failure to initialize the app", Toast.LENGTH_LONG).show();
                return;
            }
        }else{
            log("Lucy is initialized!");
        }
        initComponents();
        initListeners();
        //randomQuote();
        openDB();
    }

    private void initComponents(){
        log("...initComponents()");
        textViewToday = findViewById(R.id.homeActivity_today);
        textViewStatistics = findViewById(R.id.homeActivity_statistics);
        textViewQuote = findViewById(R.id.homeActivity_quote);
        textViewQuote.setSelected(true);
        textViewNewMain = findViewById(R.id.homeActivity_newMain);
    }
    private void initListeners(){
        log("...initListeners()");
        textViewToday.setOnClickListener(view -> startActivity(new Intent(this, TodayActivity.class)));
        textViewStatistics.setOnClickListener(view->startActivity(new Intent(this, StatisticsMain.class)));
        textViewNewMain.setOnClickListener(view->startActivity(new Intent(this, MainActivity.class)));
        textViewQuote.setOnClickListener(view->randomQuote());
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
        //log("...onCreateOptionsMenu(Menu)");
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
    private void populateCategories(){
        log("...populateCategories()");
        LocalDB db = new LocalDB(this);
        for(String category : Lucy.CATEGORIES){
            db.executeSQL(Queeries.insertCategory(category));
        }
    }
    private void resetApp(){
        log("...resetApp()");
        try {
            lucy.reset(this);
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void testCategories(){
        log("...testCategories()");
        DBAdmin.insertCategories(this);
    }
}