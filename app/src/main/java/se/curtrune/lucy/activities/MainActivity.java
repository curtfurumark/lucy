package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import se.curtrune.lucy.R;
import se.curtrune.lucy.fragments.DurationFragment;
import se.curtrune.lucy.fragments.ItemsFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Fragment currentFragment;
    private FragmentContainerView fragmentContainerView;
    public interface OnTabSelected{
        void onSelected(TabLayout.Tab tab);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        log("MainActivity.onCreate(Bundle)");
        initComponents();
        initListeners();
        initDefaultFragment();
    }
    private void initComponents(){
        log("...initComponents()");
        tabLayout = findViewById(R.id.mainActivity_tabLayout);
        fragmentContainerView = findViewById(R.id.mainActivity_fragmentContainer);
    }
    private void initDefaultFragment(){
        log("...initDefaultFragment()");
        currentFragment = new ItemsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_fragmentContainer, currentFragment).commit();

    }
    private void initListeners(){
        log("...initListeners()");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                log("...onTabSelected()");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem)", item.getTitle().toString());
        if( item.getItemId() == R.id.mainActiivity_home){
            startActivity(new Intent(this, HomeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}