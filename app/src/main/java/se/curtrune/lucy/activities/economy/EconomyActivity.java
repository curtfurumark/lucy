package se.curtrune.lucy.activities.economy;

import static se.curtrune.lucy.util.Logger.log;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import se.curtrune.lucy.R;
import se.curtrune.lucy.fragments.DurationFragment;
import se.curtrune.lucy.fragments.MentalFragment;
import se.curtrune.lucy.fragments.TopTenFragment;

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
}