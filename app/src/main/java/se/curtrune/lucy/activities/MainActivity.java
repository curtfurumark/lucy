package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.flying_fish.GameActivity;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.app.User;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.dialogs.BoostDialog;
import se.curtrune.lucy.fragments.AppointmentsFragment;
import se.curtrune.lucy.fragments.CalenderFragment;
import se.curtrune.lucy.fragments.ContactFragment;
import se.curtrune.lucy.fragments.DurationFragment;
import se.curtrune.lucy.fragments.EnchildaFragment;
import se.curtrune.lucy.fragments.EstimateFragment;
import se.curtrune.lucy.fragments.GraphFragment;
import se.curtrune.lucy.fragments.MentalFragment2;
import se.curtrune.lucy.fragments.MonthCalenderFragment;
import se.curtrune.lucy.fragments.ProjectsFragment;
import se.curtrune.lucy.fragments.TodoFragment;
import se.curtrune.lucy.fragments.TopTenFragment;
import se.curtrune.lucy.fragments.WeeklyCalenderFragment;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.AffirmationWorker;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;

public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment;
    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LucindaViewModel viewModel;
    private FloatingActionButton fapPanic;
    private FloatingActionButton fapBoost;
    private TextView textViewEnergy;
    public static boolean VERBOSE = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        log("MainActivity.onCreate(Bundle of joy)");
        initComponents();
        initListeners();
        initViewModel();
        Intent intent = getIntent();
        if( intent.getBooleanExtra(Constants.INTENT_SHOW_CHILD_ITEMS, false)){
            log("...INTENT_SHOW_CHILD_ITEMS");
            Item currentParent = (Item) intent.getSerializableExtra(Constants.INTENT_SERIALIZED_ITEM);
        }
        if( Lucinda.currentFragment != null){
            log("...currentFragment != null");
            currentFragment = Lucinda.currentFragment;
        }else{
            currentFragment = new CalenderFragment();
            navigate(currentFragment);
        }
        setUserInterfaceCurrentEnergy();
    }
    private void boostMe(){
        log("...boostMe()");
        AffirmationWorker.requestAffirmation(affirmation -> {
            log("...onRequest(Affirmation)");
            BoostDialog boostDialog = new BoostDialog(affirmation.getAffirmation());
            boostDialog.show(getSupportFragmentManager(), "boost me");
        });
    }
    private void initComponents(){
        log("...initComponents()");
        toolbar = findViewById(R.id.navigationDrawer_toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigationDrawerActivity_navigationView);
        fapPanic = findViewById(R.id.mainActivity_panic);
        fapBoost = findViewById(R.id.mainActivity_buttonBoost);
        textViewEnergy = findViewById(R.id.mainActivity_energy);
        drawerLayout = findViewById(R.id.navigationDrawer_drawerLayout);
        navigationView = findViewById(R.id.navigationDrawerActivity_navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            log("... toolbar on click");
            drawerLayout.open();
        });
        navigationView.setNavigationItemSelectedListener(item -> {
            log("...onNavigationItemSelected(MenuItem) ", Objects.requireNonNull(item.getTitle()).toString());
            if( item.getItemId() == R.id.navigationDrawer_graphFragment){
                navigate(new GraphFragment());
            }else if( item.getItemId() == R.id.navigationDrawer_graphFragment){
                navigate(new GraphFragment());
            }else if( item.getItemId() == R.id.navigationDrawer_monthCalender){
                navigate(new MonthCalenderFragment());
            }else if( item.getItemId() == R.id.bottomNavigation_today){
                navigate(new CalenderFragment());
            }else if ( item.getItemId() == R.id.navigationDrawer_topTen){
                navigate( new TopTenFragment());
            }else if( item.getItemId() == R.id.bottomNavigation_todo){
                navigate( new TodoFragment());
            }else if( item.getItemId() == R.id.bottomNavigation_appointments){
                navigate( new AppointmentsFragment());
            }else if( item.getItemId() == R.id.bottomNavigation_enchilada){
                navigate( new EnchildaFragment());
            }else if (item.getItemId() == R.id.bottomNavigation_projects){
                navigate(new ProjectsFragment());
            }else if ( item.getItemId() == R.id.navigationDrawer_durationFragment){
                navigate(new DurationFragment());
            }else if( item.getItemId() == R.id.navigationDrawer_estimateFragment){
                navigate(new EstimateFragment());
            }else if( item.getItemId() == R.id.navigationDrawer_weekly){
                navigate(new WeeklyCalenderFragment());
            }else if( item.getItemId() == R.id.navigationDrawer_contactFragment){
                navigate(new ContactFragment());
            }
            drawerLayout.close();
            return true;
        });
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        textViewEnergy.setOnClickListener(view->showMentalDay());
        fapBoost.setOnClickListener(view->boostMe());
        fapPanic.setOnClickListener(view->panic());
    }
    private void initViewModel(){
        log("...initViewModel()");
        viewModel = new ViewModelProvider(this ).get(LucindaViewModel.class);
        viewModel.getEnergy().observe(this, energy->{
            log("...energy updated", energy);
            setUserInterfaceCurrentEnergy();
        });
    }
    private void navigate(Fragment fragment){
        log("...navigate(Fragment) ");
        if(fragment instanceof AppointmentsFragment)log("AppointmentsFragment");
        if(fragment instanceof ProjectsFragment)log("ProjectsFragment");
        if(fragment instanceof CalenderFragment)log("CalenderFragment");
        if(fragment instanceof TodoFragment)log("TodoFragment");
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.navigationDrawer_frameContainer, currentFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem item)", Objects.requireNonNull(item.getTitle()).toString());
        if( item.getItemId() == R.id.navigationDrawer_graphFragment){
            navigate( new GraphFragment());
        }else if( item.getItemId() == R.id.mainActivity_home){
            startActivity(new Intent(this, HomeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void openWebPage(String url){
        log("...openWebPage(String url)", url);
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        startActivity(intent);
    }
    private void panic(){
        log("...panic()");
        Toast.makeText(this, "DON'T PANIC!", Toast.LENGTH_LONG).show();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String panicAction = prefs.getString("pref_panic_action", "");
        log("....panicAction", panicAction);
        switch (panicAction) {
            case "flying fish":
                startActivity(new Intent(this, GameActivity.class));
                break;
            case "url":
                String url = User.getRandomPanicUrl();
                openWebPage(url);
                break;
            case "panic list":
                Item panicRoot = ItemsWorker.getPanicRoot(this);
                if (panicRoot == null) {
                    log("ERROR...panicRoot == null");
                } else {
                    Intent intent = new Intent(this, SequenceActivity.class);
                    intent.putExtra(Constants.INTENT_SEQUENCE_PARENT, panicRoot);
                    startActivity(intent);
                }
                break;
        }
    }
    private void setUserInterfaceCurrentEnergy(){
        log("...setUserInterfaceCurrentEnergy()");
        int energy = MentalWorker.getEnergy(LocalDate.now(), this);
        String textEnergy = String.format(Locale.getDefault(), "energy: %d", energy);
        if( energy <= -3){
            textViewEnergy.setTextColor(Color.parseColor("#ff0000"));
        }else if(energy <= 2){
            textViewEnergy.setTextColor(Color.parseColor("#ffff00"));
        }else{
            textViewEnergy.setTextColor(Color.parseColor("#00ff00"));
        }
        textViewEnergy.setText(textEnergy);
    }
    private void showMentalDay(){
        log("...showMentalDay");
        navigate(new MentalFragment2());
    }
}