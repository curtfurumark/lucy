package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.persist.DBAdmin;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;
import se.curtrune.lucy.util.Logger;
import se.curtrune.lucy.util.Settings;

public class HomeActivity extends AppCompatActivity {
    private TextView textViewToday;
    private TextView textViewProjects;
    private TextView textViewMental;
    private TextView textViewStatistics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        setTitle("rocky road planner");
        log("HomeActivity.onCreate(Bundle)");
        initComponents();
        initListeners();
        openDB();
    }

    private void initComponents(){
        log("...initComponents()");
        textViewToday = findViewById(R.id.homeActivity_today);
        textViewProjects = findViewById(R.id.homeActivity_projects);
        textViewMental = findViewById(R.id.homeActivity_mental);
        textViewStatistics = findViewById(R.id.homeActivity_statistics);
    }
    private void initListeners(){
        log("...initListeners()");
        textViewToday.setOnClickListener(view -> startActivity(new Intent(this, TodayActivity.class)));
        textViewProjects.setOnClickListener(v -> startActivity(new Intent(this, ItemsActivity.class)));
        textViewStatistics.setOnClickListener(view->startActivity(new Intent(this, StatisticsActivity.class)));
        textViewMental.setOnClickListener(v->startActivity(new Intent(this, MentalListActivity.class)));
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
        //return super.onCreateOptionsMenu(menu);
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
        //return super.onOptionsItemSelected(item);
    }

    private void openDB(){
        log("...openDB");
        LocalDB db = new LocalDB(this);
        db.open();
    }
    private void deleteItemsTable(){
        log("...deleteItemsTable()");
        LocalDB db = new LocalDB(this);
        db.executeSQL(Queeries.DELETE_ITEMS_TABLE);

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
        for(String category : Settings.CATEGORIES){
            db.executeSQL(Queeries.insertCategory(category));
        }
    }
    private void resetApp(){
        log("...resetApp()");
    }
}