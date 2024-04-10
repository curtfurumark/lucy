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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;

import se.curtrune.lucy.R;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.fragments.CalenderFragment;
import se.curtrune.lucy.fragments.ProjectsFragment;
import se.curtrune.lucy.fragments.TodoFragment;
import se.curtrune.lucy.util.Constants;

public class MainActivity extends AppCompatActivity {

    //private TabLayout tabLayout;
    private Fragment currentFragment;
    private BottomNavigationView bottomNavigation;
    private FragmentContainerView fragmentContainerView;
    public interface OnTabSelected{
        void onSelected(TabLayout.Tab tab);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        log("MainActivity.onCreate(Bundle)");
        setTitle(LocalDate.now().toString());
        initComponents();
        initListeners();
        initDefaultFragment();
        Intent intent = getIntent();
        if( intent.getBooleanExtra(Constants.INTENT_SHOW_CHILD_ITEMS, false)){
            log("...INTENT_SHOW_CHILD_ITEMS");
        }

        if(Lucinda.currentFragment == null) {
            currentFragment = new CalenderFragment();
        }
        navigate(currentFragment);
        setBottomNavigationSelected(currentFragment);
    }
    private void initComponents(){
        log("...initComponents()");
        //tabLayout = findViewById(R.id.mainActivity_tabLayout);
        fragmentContainerView = findViewById(R.id.mainActivity_fragmentContainer);
        bottomNavigation = findViewById(R.id.mainActivity_bottomNavigation);
    }
    private void initDefaultFragment(){
        log("...initDefaultFragment()");
        currentFragment = new ProjectsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_fragmentContainer, currentFragment).commit();

    }
    private void initListeners(){
        log("...initListeners()");
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                log("...onNavigationItemSelected(MenuItem)");
                if( item.getItemId() == R.id.bottomNavigation_today){
                    navigate(new CalenderFragment());
                }else if( item.getItemId() == R.id.bottomNavigation_projects){
                    navigate(new ProjectsFragment());
                }else if( item.getItemId() == R.id.bottomNavigation_todo){
                    navigate(new TodoFragment());
                }
                return true;
            }
        });
/*        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                log("...onTabSelected(Tab)");
                if( tab.getText().toString().equals("projects")){
                    log("...load projects");
                }else if( tab.getText().toString().equals("today")){
                    navigate(new CalenderFragment());
                }
            }*/

/*            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }
    private void navigate(Fragment fragment){
        log("navigate(Fragment) ");
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_fragmentContainer, currentFragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem)", item.getTitle().toString());
        if( item.getItemId() == R.id.mainActivity_home){
            startActivity(new Intent(this, HomeActivity.class));
        }/*else if( item.getItemId() == R.id.mainActivity_startSequence){
            startSequence();
        }*/
        return super.onOptionsItemSelected(item);
    }
    private void setBottomNavigationSelected(Fragment fragment){
        int selectedItemID = 0;
        if( fragment instanceof ProjectsFragment){
            selectedItemID = R.id.bottomNavigation_projects;
        }else if( fragment instanceof TodoFragment){
            selectedItemID = R.id.bottomNavigation_todo;
        }else{
            selectedItemID = R.id.bottomNavigation_today;
        }
        bottomNavigation.setSelectedItemId(selectedItemID);
    }
    private void startSequence(){
        log("...startSequence()");
        Intent intent = new Intent(this, SequenceActivity.class);
        //intent.putExtra(Constants.INTENT_SEQUENCE_PARENT)

    }
}