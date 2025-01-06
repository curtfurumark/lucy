package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.flying_fish.GameActivity;
import se.curtrune.lucy.activities.kotlin.IndexActivityKt;
import se.curtrune.lucy.app.FirstPage;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.app.User;
import se.curtrune.lucy.classes.Affirmation;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.dialogs.BoostDialog;
import se.curtrune.lucy.dialogs.PanicActionDialog;
import se.curtrune.lucy.dialogs.UpdateDialog;
import se.curtrune.lucy.fragments.AppointmentsFragment;
import se.curtrune.lucy.fragments.CalendarWeekHostFragment;
import se.curtrune.lucy.fragments.CalenderDateFragment;
import se.curtrune.lucy.fragments.ContactFragment;
import se.curtrune.lucy.fragments.ContactsFragment;
import se.curtrune.lucy.fragments.CustomizeFragment;
import se.curtrune.lucy.fragments.DailyGraphFragment;
import se.curtrune.lucy.fragments.DurationFragment;
import se.curtrune.lucy.fragments.EnchiladaFragment;
import se.curtrune.lucy.fragments.EstimateFragment;
import se.curtrune.lucy.activities.kotlin.monthcalendar.MonthFragment;
import se.curtrune.lucy.fragments.ProjectsFragment;
import se.curtrune.lucy.fragments.MentalDateFragment;
import se.curtrune.lucy.fragments.MentaHistoryFragment;
import se.curtrune.lucy.fragments.MessageBoardFragment;
import se.curtrune.lucy.fragments.SequenceFragment;
import se.curtrune.lucy.fragments.TimerFragment;
import se.curtrune.lucy.fragments.TodoFragment;
import se.curtrune.lucy.fragments.TopTenFragment;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.web.VersionInfo;
import se.curtrune.lucy.workers.InternetWorker;
import se.curtrune.lucy.workers.ItemsWorker;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private LucindaViewModel viewModel;
    private Fragment currentFragment;
    private TextView textViewPanic;
    private TextView textViewBoost;
    private TextView textViewEnergy;
    private TextView textViewLucindaHome;
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
        String fragmentName = intent.getStringExtra(Constants.MAIN_ACTIVITY_CHILD_FRAGMENT);
        if(fragmentName != null){
            log("...fragmentName", fragmentName);
            navigate(FirstPage.valueOf(fragmentName));
        }else {
            navigate(new CalenderDateFragment());
        }
    }
    private void boostMe(){
        if( VERBOSE) log("...boostMe()");
        viewModel.requestAffirmation();
    }

    private void initComponents(){
        if( VERBOSE) log("...initComponents()");
        MaterialToolbar toolbar = findViewById(R.id.navigationDrawer_toolbar);
        setSupportActionBar(toolbar);
        textViewPanic = findViewById(R.id.mainActivity_panic);
        textViewBoost = findViewById(R.id.mainActivity_buttonBoost);
        textViewEnergy = findViewById(R.id.mainActivity_energy);
        drawerLayout = findViewById(R.id.navigationDrawer_drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationDrawerActivity_navigationView);
        View view = navigationView.inflateHeaderView(R.layout.navigation_header);
        textViewLucindaHome = view.findViewById(R.id.navigationHeader_lucindaHome);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            if( VERBOSE) log("... toolbar on click");
            drawerLayout.open();
        });
        NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = item -> {
            log("...onNavigationItemSelected(MenuItem) ", Objects.requireNonNull(item.getTitle()).toString());
            if (item.getItemId() == R.id.navigationDrawer_graphFragment) {
                navigate(new DailyGraphFragment());
            } else if (item.getItemId() == R.id.navigationDrawer_monthCalender) {
                //navigate(new CalendarMonthHostFragment());
                navigate(new MonthFragment());
            } else if (item.getItemId() == R.id.bottomNavigation_today) {
                navigate(new CalenderDateFragment());
            } else if (item.getItemId() == R.id.navigationDrawer_topTen) {
                navigate(new TopTenFragment());
            } else if (item.getItemId() == R.id.bottomNavigation_todo) {
                navigate(new TodoFragment());
            } else if (item.getItemId() == R.id.bottomNavigation_appointments) {
                navigate(new AppointmentsFragment());
            } else if (item.getItemId() == R.id.bottomNavigation_enchilada) {
                navigate(new EnchiladaFragment());
            } else if (item.getItemId() == R.id.bottomNavigation_projects) {
                navigate(new ProjectsFragment());
            } else if (item.getItemId() == R.id.navigationDrawer_durationFragment) {
                navigate(new DurationFragment());
            } else if (item.getItemId() == R.id.navigationDrawer_estimateFragment) {
                navigate(new EstimateFragment());
            } else if (item.getItemId() == R.id.navigationDrawer_weekly) {
                navigate(new CalendarWeekHostFragment());
            } else if (item.getItemId() == R.id.navigationDrawer_contactFragment) {
                navigate(new ContactFragment());
            } else if (item.getItemId() == R.id.navigationDrawer_messageBoardFragment) {
                navigate(new MessageBoardFragment());
            } else if (item.getItemId() == R.id.navigationDrawer_countDownTimer) {
                navigate(new TimerFragment());
            } else if (item.getItemId() == R.id.navigationDrawer_customizeFragment) {
                navigate(new CustomizeFragment());
            } else if (item.getItemId() == R.id.navigationDrawer_mentalFragment) {
                navigate(new MentalDateFragment());
            } else if (item.getItemId() == R.id.navigationDrawer_mentalHistoryFragment) {
                navigate(new MentaHistoryFragment());
            }else if(item.getItemId() == R.id.navigationDrawer_contactsFragment){
                navigate(new ContactsFragment());
            } else if (item.getItemId() == R.id.navigationDrawer_logOut) {
                log("...log out");
                Intent intent = new Intent(this, LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
            drawerLayout.close();
            return true;
        };
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        textViewEnergy.setOnClickListener(view->showMentalDay());
        textViewBoost.setOnClickListener(view->boostMe());
        textViewPanic.setOnClickListener(view->showPanicAction());
        textViewLucindaHome.setOnClickListener(view->openWebPage("https://curtfurumark.se/lucinda"));
    }
    private void initViewModel(){
        if( VERBOSE) log("...initViewModel()");
        viewModel = new ViewModelProvider(this ).get(LucindaViewModel.class);
        viewModel.getFragment().observe(this, fragment -> {
            log(" new fragment observed");
            navigate(fragment);});
        viewModel.init(LocalDate.now(), this);
        viewModel.getEnergy().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer energy) {
                log("...onChanged(Integer) energy", energy);
                setTextViewMental(getString(R.string.energy), energy);
            }
        });
        viewModel.getAnxiety().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer anxiety) {
                log("...anxiety.onChanged(Integer)", anxiety);
                setTextViewMental(getString(R.string.anxiety), anxiety);
            }
        });
        viewModel.getStress().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer stress) {
                log("...stress.onChanged(Integer)", stress);
                setTextViewMental(getString(R.string.stress), stress);
            }
        });
        viewModel.getMood().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer mood) {
                log("...mood.onChanged(Integer)", mood);
                setTextViewMental(getString(R.string.mood), mood);
            }
        });
        viewModel.getAffirmation().observe(this, new Observer<Affirmation>() {
            @Override
            public void onChanged(Affirmation affirmation) {
                showBoostDialog(affirmation);
            }
        });
        viewModel.updateAvailable().observe(this, new Observer<VersionInfo>() {
            @Override
            public void onChanged(VersionInfo versionInfo) {
                log("...updateAvailable(VersionInfo)");
                UpdateDialog dialog = new UpdateDialog(versionInfo, new UpdateDialog.Callback() {
                    @Override
                    public void onClick() {
                        log("here we go");
                        Toast.makeText(getApplicationContext(), "here we go,", Toast.LENGTH_LONG).show();
                        log("updated url" ,versionInfo.getUrl());
                        openWebPage(versionInfo.getUrl());
                    }
                });
                dialog.show(getSupportFragmentManager(), "update lucinda");
            }
        });
        viewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * @param fragment, the fragment to go to
     */
    private void navigate(Fragment fragment){
        if( fragment == null){
            log("...navigate(Fragment) called with null fragment, i surrender");
            return;
        }
        log("MainActivity.navigate(Fragment) ", fragment.getClass().getName());
        currentFragment = fragment;
        //setTextViewMental(getString(R.string.energy), viewModel.getEnergy().getValue());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navigationDrawer_frameContainer, fragment)
                .addToBackStack("previous fragment")
                .commit();
    }
    private void navigate(FirstPage firstPage){
        log("...navigate(FirstPage)", firstPage.toString());
        switch (firstPage){
            case CALENDER_DATE:
                navigate(new CalenderDateFragment());
                break;
            case CALENDER_WEEK:
                navigate(new CalendarWeekHostFragment());
                break;
            case CALENDER_MONTH:
                navigate( new MonthFragment());
                break;
            case CALENDER_APPOINTMENTS:
                navigate( new AppointmentsFragment());
                break;
            case TODO_FRAGMENT:
                navigate( new TodoFragment());
                break;
            default:
                log("WARNING UNKNOWN FRAGMENT");

        }
    }

    @Override
    public void onBackPressed() {
        log("MainActivity.onBackPressed()");
        //super.onBackPressed();
        if( drawerLayout.isDrawerOpen(GravityCompat.START)){
            log("...drawerLayout is open, closing");
            drawerLayout.close();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.mainActivity_search).getActionView();
        searchView.setQueryHint("search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                log("...onQueryTextSubmit(String)", query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String filter) {
                log("...onQueryTextChange(String)", filter);
                viewModel.filter(filter);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem item)", Objects.requireNonNull(item.getTitle()).toString());
        if( item.getItemId() == R.id.navigationDrawer_graphFragment){
            navigate( new DailyGraphFragment());
        }else if( item.getItemId() == R.id.mainActivity_dev) {
            startActivity(new Intent(this, DevActivity.class));
        }else if( item.getItemId() == R.id.mainActivityCheckForUpdate){
            viewModel.checkIfUpdateAvailable(this);
        }else if( item.getItemId() == R.id.mainActivity_calendar){
            navigate(new CalenderDateFragment());
        }else if( item.getItemId() == R.id.mainActivity_lucinda20){
            startActivity(new Intent(this, IndexActivityKt.class));
        }
        return super.onOptionsItemSelected(item);
    }



    private void openWebPage(String url){
        log("...openWebPage(String url)", url);
        if(!InternetWorker.isConnected(this)){
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            return;
        }
        Uri webPage = Uri.parse(url);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
            startActivity(intent);
        }catch (Exception e){
            log("EXCEPTION, probably not valid ur");
            Toast.makeText(this,"page not found", Toast.LENGTH_LONG).show();
        }
    }
    private void panicActionICE(){
        log("...panicActionICE");
        //FIX PERMISSION, IN CUSTOMIZEFRAGEMENT PERHAPS
/*        int phoneNumber = User.getICE(this);
        if( phoneNumber == -1){
            Toast.makeText(this, "no phone number to call", Toast.LENGTH_LONG).setMentalType();
            return;
        }
        String stringURI = String.format(Locale.getDefault(), "tel:%d", phoneNumber);
        log("...stringURI", stringURI);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(stringURI));
        startActivity(intent)*/
    }
    private void panicActionPending(){
        log("...panicActionPending()");
        PanicActionDialog dialog = new PanicActionDialog();
        dialog.setListener(new PanicActionDialog.Listener() {
            @Override
            public void onPanicAction(Settings.PanicAction panicAction) {
                log("onPanicAction(PanicAction)");
                panic(panicAction);

            }
        });
        dialog.show(getSupportFragmentManager(),"panic action");
    }
    private void panic(Settings.PanicAction panicAction){
        log("...panic(PanicAction))");
        log("....panicAction", panicAction.toString());
        switch (panicAction) {
            case GAME:
                startActivity(new Intent(this, GameActivity.class));
                break;
            case URL:
                String url = User.getRandomPanicUrl(this);
                openWebPage(url);
                break;
            case SEQUENCE:
                Item panicRoot = ItemsWorker.getPanicRoot(this);
                if (panicRoot == null) {
                    log("ERROR...panicRoot == null");
                } else {
                    navigate(new SequenceFragment(panicRoot));
                }
                break;
            case ICE:
                panicActionICE();
                break;
            case PENDING:
                panicActionPending();
                break;
            default:
                //TODO, translate
                Toast.makeText(this, "go to customize and set preferred panic action", Toast.LENGTH_LONG).show();
                break;
        }
    }
    private void setTextViewMental(String label, int value){
        log("MainActivity.setTextViewMental(String, int)", value);
        String strMentalState = String.format(Locale.getDefault(), "%s: %d",label ,value);
        if( value <= -3){
            textViewEnergy.setTextColor(Color.parseColor("#ff0000"));
        }else if(value <= 2){
            textViewEnergy.setTextColor(Color.parseColor("#ffff00"));
        }else{
            textViewEnergy.setTextColor(Color.parseColor("#00ff00"));
        }
        textViewEnergy.setText(strMentalState);

    }

    private void showBoostDialog(Affirmation affirmation){
        log("...showBoostDialog(Affirmation)");
        BoostDialog boostDialog = new BoostDialog(affirmation.getAffirmation());
        boostDialog.show(getSupportFragmentManager(), "boost me");
    }

    /**
     * loads fragment, stats about current day,
     * expected/estimated duration and mental and actual duration and mental
     */
    private void showMentalDay(){
        log("...showMentalDay");
        viewModel.toggleRecyclerMode();
    }
    private void showPanicAction(){
        Settings.PanicAction panicAction = viewModel.getPanicAction(this);
        log("...showPanicAction()", panicAction.toString());
        switch (panicAction) {
            case GAME:
                startActivity(new Intent(this, GameActivity.class));
                break;
            case URL:
                String url = User.getRandomPanicUrl(this);
                openWebPage(url);
                break;
            case SEQUENCE:
                Item panicRoot = ItemsWorker.getPanicRoot(this);
                if (panicRoot == null) {
                    log("ERROR...panicRoot == null");
                } else {
                    navigate(new SequenceFragment(panicRoot));
                }
                break;
            case ICE:
                panicActionICE();
                break;
            case PENDING:
                panicActionPending();
                break;
            default:
                //TODO, translate
                Toast.makeText(this, "go to customize and set preferred panic action", Toast.LENGTH_LONG).show();
                break;
        }
    }
}