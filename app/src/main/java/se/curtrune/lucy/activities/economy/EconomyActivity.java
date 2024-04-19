package se.curtrune.lucy.activities.economy;

import static se.curtrune.lucy.util.Logger.log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.HomeActivity;
import se.curtrune.lucy.activities.economy.persist.ECDBAdmin;
import se.curtrune.lucy.fragments.DurationFragment;
import se.curtrune.lucy.fragments.MentalFragment;
import se.curtrune.lucy.fragments.TopTenFragment;
import se.curtrune.lucy.persist.DBAdmin;

public class EconomyActivity extends AppCompatActivity {
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("EconomyActivity.onCreate()");
        setContentView(R.layout.economy_activity);
        initComponents();
        initListeners();
    }
    private void createTables(){
        log("...createTables()");
        ECDBAdmin.createEconomyTables(this);
    }
    private void dropTables(){
        log("...dropTables()");
        ECDBAdmin.dropTables(this);
    }
    private void initComponents(){
        log("...initComponents()");
        tabLayout = findViewById(R.id.economyActivity_tabLayout);
    }
    private void initListeners(){
        log("...initListeners()");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position =  tab.getPosition();
                log("...onTabSelected(Tab) position", position);
                switch (position){
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.economyActivity_fragmentContainer, new TransactionFragment()).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.economyActivity_fragmentContainer, new AssetsFragment()).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.economyActivity_fragmentContainer, new EconomyStatisticsFragment()).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                log("...onTabUnselected(TabLayout)");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                log("...onTabReselected(TabLayout)");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.economy_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem)");
        if( item.getItemId() == R.id.economyActivity_home){
            startActivity(new Intent(this, HomeActivity.class));
        }else if (item.getItemId() == R.id.economyActivity_dropTables){
            dropTables();
        }else if( item.getItemId() == R.id.economyActivity_createTables){
            createTables();
        }
        return super.onOptionsItemSelected(item);
    }
}