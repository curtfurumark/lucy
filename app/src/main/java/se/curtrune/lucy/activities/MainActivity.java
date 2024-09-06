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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.flying_fish.GameActivity;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.app.User;
import se.curtrune.lucy.classes.Affirmation;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.dialogs.BoostDialog;
import se.curtrune.lucy.dialogs.PanicActionDialog;
import se.curtrune.lucy.fragments.AppointmentsFragment;
import se.curtrune.lucy.fragments.CalenderDateFragment;
import se.curtrune.lucy.fragments.ContactFragment;
import se.curtrune.lucy.fragments.CustomizeFragment;
import se.curtrune.lucy.fragments.DailyGraphFragment;
import se.curtrune.lucy.fragments.DurationFragment;
import se.curtrune.lucy.fragments.EnchiladaFragment;
import se.curtrune.lucy.fragments.EstimateFragment;
import se.curtrune.lucy.fragments.MentalDayFragment;
import se.curtrune.lucy.fragments.MentaHistoryFragment;
import se.curtrune.lucy.fragments.MessageBoardFragment;
import se.curtrune.lucy.fragments.MonthCalenderFragment;
import se.curtrune.lucy.fragments.ProjectsFragment;
import se.curtrune.lucy.fragments.SequenceFragment;
import se.curtrune.lucy.fragments.TimerFragment;
import se.curtrune.lucy.fragments.TodoFragment;
import se.curtrune.lucy.fragments.TopTenFragment;
import se.curtrune.lucy.fragments.CalenderWeekFragment;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.AffirmationWorker;
import se.curtrune.lucy.workers.InternetWorker;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private LucindaViewModel viewModel;
    private FloatingActionButton fapPanic;
    private FloatingActionButton fapBoost;
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
        navigate(new CalenderDateFragment());
        //setUserInterfaceCurrentEnergy();
    }
    private void boostMe(){
        if( VERBOSE) log("...boostMe()");
        AffirmationWorker.requestAffirmation(new AffirmationWorker.RequestAffirmationCallback() {
            @Override
            public void onRequest(Affirmation affirmation) {
                log("...onRequest(Affirmation)");
                BoostDialog boostDialog = new BoostDialog(affirmation.getAffirmation());
                boostDialog.show(getSupportFragmentManager(), "boost me");
            }

            @Override
            public void onError(String message) {
                Toast.makeText( MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initComponents(){
        if( VERBOSE) log("...initComponents()");
        MaterialToolbar toolbar = findViewById(R.id.navigationDrawer_toolbar);
        setSupportActionBar(toolbar);
        fapPanic = findViewById(R.id.mainActivity_panic);
        fapBoost = findViewById(R.id.mainActivity_buttonBoost);
        textViewEnergy = findViewById(R.id.mainActivity_energy);
        drawerLayout = findViewById(R.id.navigationDrawer_drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationDrawerActivity_navigationView);;
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
        navigationView.setNavigationItemSelectedListener(item -> {
            log("...onNavigationItemSelected(MenuItem) ", Objects.requireNonNull(item.getTitle()).toString());
            if( item.getItemId() == R.id.navigationDrawer_graphFragment){
                navigate(new DailyGraphFragment());
            }else if( item.getItemId() == R.id.navigationDrawer_monthCalender){
                navigate(new MonthCalenderFragment());
            }else if( item.getItemId() == R.id.bottomNavigation_today){
                navigate(new CalenderDateFragment());
            }else if ( item.getItemId() == R.id.navigationDrawer_topTen){
                navigate( new TopTenFragment());
            }else if( item.getItemId() == R.id.bottomNavigation_todo){
                navigate( new TodoFragment());
            }else if( item.getItemId() == R.id.bottomNavigation_appointments){
                navigate( new AppointmentsFragment());
            }else if( item.getItemId() == R.id.bottomNavigation_enchilada){
                navigate( new EnchiladaFragment());
            }else if (item.getItemId() == R.id.bottomNavigation_projects){
                navigate(new ProjectsFragment());
            }else if ( item.getItemId() == R.id.navigationDrawer_durationFragment){
                navigate(new DurationFragment());
            }else if( item.getItemId() == R.id.navigationDrawer_estimateFragment){
                navigate(new EstimateFragment());
            }else if( item.getItemId() == R.id.navigationDrawer_weekly){
                navigate(new CalenderWeekFragment());
            }else if( item.getItemId() == R.id.navigationDrawer_contactFragment){
                navigate(new ContactFragment());
            }else if( item.getItemId() == R.id.navigationDrawer_messageBoardFragment){
                navigate(new MessageBoardFragment());
            }else if( item.getItemId() == R.id.navigationDrawer_countDownTimer){
                navigate(new TimerFragment());
            }else if ( item.getItemId() == R.id.navigationDrawer_customizeFragment){
                navigate(new CustomizeFragment());
            }else if( item.getItemId() == R.id.navigationDrawer_mentalFragment) {
                navigate(new MentalDayFragment());
            }else if( item.getItemId() == R.id.navigationDrawer_mentalHistoryFragment){
                log("mental history fragment");
                navigate(new MentaHistoryFragment());
            }else if( item.getItemId() ==R.id.navigationDrawer_logOut){
                log("...log out");
                Intent intent = new Intent(this, LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
            drawerLayout.close();
            return true;
        });
    }


    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        textViewEnergy.setOnClickListener(view->showMentalDay());
        fapBoost.setOnClickListener(view->boostMe());
        fapPanic.setOnClickListener(view->panic(User.getPanicAction(this)));
        textViewLucindaHome.setOnClickListener(view->openWebPage("https://curtfurumark.se/lucinda"));
    }
    private void initViewModel(){
        if( VERBOSE) log("...initViewModel()");
        viewModel = new ViewModelProvider(this ).get(LucindaViewModel.class);
        viewModel.updateEnergy().observe(this, energy->{
            log("...energy updated", energy);
            setUserInterfaceCurrentEnergy();
        });
        viewModel.getFragment().observe(this, fragment -> navigate(fragment));
    }

    /**
     * sorry this one does two things
     * navigates to fragment
     * and sets current energy level ui
     * @param fragment
     */
    private void navigate(Fragment fragment){
        if( fragment == null){
            log("...navigate(Fragment) called with null fragment, i surrender");
            return;
        }
        setUserInterfaceCurrentEnergy();
        log("MainActivity.navigate(Fragment) ", fragment.getClass().getName());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navigationDrawer_frameContainer, fragment)
                .addToBackStack("previous fragment")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem item)", Objects.requireNonNull(item.getTitle()).toString());
        //TODO, remove
        if( item.getItemId() == R.id.navigationDrawer_graphFragment){
            navigate( new DailyGraphFragment());
        }else if( item.getItemId() == R.id.mainActivity_dev){
            startActivity(new Intent(this, DevActivity.class));
        }else if( item.getItemId() == R.id.mainActivity_calendar){
            navigate(new CalenderDateFragment());
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
            Toast.makeText(this, "no phone number to call", Toast.LENGTH_LONG).show();
            return;
        }
        String stringURI = String.format(Locale.getDefault(), "tel:%d", phoneNumber);
        log("...stringURI", stringURI);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(stringURI));
        startActivity(intent)*/;
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
        //Toast.makeText(this, "DON'T PANIC!", Toast.LENGTH_LONG).show();
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
    private void setUserInterfaceCurrentEnergy(){
        log("...setUserInterfaceCurrentEnergy()");
        int energy = MentalWorker.getEnergy(LocalDate.now(), this);
        String textEnergy = String.format(Locale.getDefault(), "%s: %d",getString(R.string.energy) ,energy);
        if( energy <= -3){
            textViewEnergy.setTextColor(Color.parseColor("#ff0000"));
        }else if(energy <= 2){
            textViewEnergy.setTextColor(Color.parseColor("#ffff00"));
        }else{
            textViewEnergy.setTextColor(Color.parseColor("#00ff00"));
        }
        textViewEnergy.setText(textEnergy);
    }

    /**
     * loads fragment, stats about current day,
     * expected/estimated duration and mental and actual duration and mental
     */
    private void showMentalDay(){
        log("...showMentalDay");
        navigate(new EstimateFragment());
    }
}