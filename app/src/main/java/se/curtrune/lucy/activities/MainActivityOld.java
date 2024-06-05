package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.classes.Affirmation;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.activities.flying_fish.GameActivity;
import se.curtrune.lucy.dialogs.BoostDialog;
import se.curtrune.lucy.fragments.AppointmentsFragment;
import se.curtrune.lucy.fragments.CalenderFragment;
import se.curtrune.lucy.fragments.EnchildaFragment;
import se.curtrune.lucy.fragments.MentalFragment2;
import se.curtrune.lucy.fragments.ProjectsFragment;
import se.curtrune.lucy.fragments.TodoFragment;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.AffirmationWorker;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;

public class MainActivityOld extends AppCompatActivity {

    private LucindaViewModel viewModel;
    private Fragment currentFragment;
    private final static String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fapPanic;
    private FloatingActionButton fapBoost;
    private TextView textViewEnergy;
    private FragmentContainerView fragmentContainerView;
    public static boolean VERBOSE = false;
    public interface OnTabSelected{
        void onSelected(TabLayout.Tab tab);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_old);
        log("MainActivityOld.onCreate(Bundle)");
        int currentEnergy = MentalWorker.getEnergy(LocalDate.now(), this);
        String energyTitle = String.format(Locale.getDefault(), "energy %d", currentEnergy);
        setTitle(energyTitle);
        initComponents();
        initListeners();
        initViewModel();
        Intent intent = getIntent();
        if( Lucinda.currentFragment != null){
            log("...currentFragment != null");
            currentFragment = Lucinda.currentFragment;
        }else{
            currentFragment = new CalenderFragment();
        }
        setBottomNavigationSelected(currentFragment);
        setUserInterfaceCurrentEnergy();
    }
    private void boostMe(){
        log("...boostMe()");
/*        AffirmationWorker.requestAffirmation(new AffirmationWorker.RequestAffirmationCallback() {
            @Override
            public void onRequest(Affirmation affirmation) {
                log("...onRequest(Affirmation)");
                BoostDialog boostDialog = new BoostDialog(affirmation.getAffirmation());
                boostDialog.show(getSupportFragmentManager(), "boost me");
            }

            @Override
            public void onException(Exception e) {
                //Toast.makeText()
            }
        });*/
    }
    private void calculateEstimate(){
        log("...calculateEstimate()");
        //EstimateDate estimateDate = new EstimateDate(items)

    }
    private void initComponents(){
        if( VERBOSE) log("...initComponents()");
        //tabLayout = findViewById(R.id.mainActivity_tabLayout);
        fragmentContainerView = findViewById(R.id.mainActivity_fragmentContainer);
        bottomNavigation = findViewById(R.id.mainActivity_bottomNavigation);
        fapPanic = findViewById(R.id.mainActivity_panic);
        fapBoost = findViewById(R.id.mainActivity_buttonBoost);
        textViewEnergy = findViewById(R.id.mainActivity_energy);
    }
    private void initDefaultFragment(){
        log("...initDefaultFragment()");
        currentFragment = new ProjectsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_fragmentContainer, currentFragment).commit();
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        textViewEnergy.setOnClickListener(view->showMentalDay());
        fapBoost.setOnClickListener(view->boostMe());
        fapPanic.setOnClickListener(view->panic());
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                log("...onNavigationItemSelected(MenuItem)", item.getTitle().toString());
                if( item.getItemId() == R.id.bottomNavigation_today){
                    navigate(new CalenderFragment());
                }else if( item.getItemId() == R.id.bottomNavigation_projects){
                    navigate(new ProjectsFragment());
                }else if( item.getItemId() == R.id.bottomNavigation_todo){
                    navigate(new TodoFragment());
                }else if( item.getItemId() == R.id.bottomNavigation_enchilada){
                    navigate(new EnchildaFragment());
                }else if( item.getItemId() == R.id.bottomNavigation_appointments){
                    navigate(new AppointmentsFragment());
                }
                setUserInterfaceCurrentEnergy();
                return true;
            }
        });
    }
    private void initViewModel(){
        log("...initViewModel()");
        viewModel = new ViewModelProvider(this ).get(LucindaViewModel.class);
        viewModel.getEnergy().observe(this, energy->{
            log("...energy updated", energy);
            setUserInterfaceCurrentEnergy();
        });
        viewModel.getUpdateEnergy().observe(this , update->{
            log("...getUpdateEnergy().observe");
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
        if( item.getItemId() == R.id.mainActivity_dev) {
            startActivity(new Intent(this, DevActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        log("MainActivityOld.onSaveInstanceState(Bundle of joy)");
        Lucinda.currentFragment = currentFragment;
        //outState.putParcelable(CURRENT_FRAGMENT, (Parcelable) currentFragment);
        super.onSaveInstanceState(outState);
    }
    private void panic(){
        log("...panic()");
        Toast.makeText(this, "DON'T PANIC!", Toast.LENGTH_LONG).show();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String panicAction = prefs.getString("pref_panic_action", "");
        log("....panicAction", panicAction);
        if( panicAction.equals("flying fish")){
            startActivity(new Intent(this, GameActivity.class));
        }else if(panicAction.equals("url")){
            openWebPage("https://bongo.cat");
        }else if(panicAction.equals("panic list")){
            Item panicRoot = ItemsWorker.getPanicRoot(this);
            if( panicRoot == null){
                log("ERROR...panicRoot == null");
            }else {
                Intent intent = new Intent(this, SequenceActivity.class);
                intent.putExtra(Constants.INTENT_SEQUENCE_PARENT, panicRoot);
                startActivity(intent);
            }
        }
    }
    private void openWebPage(String url){
        log("...openWebPage(String url)", url);
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        startActivity(intent);
/*        if( intent.resolveActivity(getPackageManager()) != null){
            log("...will save activity");
            startActivity(intent);
        }else{
            log("...unable to resolve activity");
        }*/

    }

    private void setBottomNavigationSelected(Fragment fragment){
        int selectedItemID = 0;
        if( fragment instanceof ProjectsFragment){
            selectedItemID = R.id.bottomNavigation_projects;
        }else if( fragment instanceof TodoFragment) {
            selectedItemID = R.id.bottomNavigation_todo;
        }else if ( fragment instanceof EnchildaFragment){
            selectedItemID = R.id.bottomNavigation_enchilada;
        }else if( fragment instanceof  AppointmentsFragment) {
            selectedItemID = R.id.bottomNavigation_appointments;
        }else{
            selectedItemID = R.id.bottomNavigation_today;
        }
        bottomNavigation.setSelectedItemId(selectedItemID);
    }
    private void setUserInterfaceCurrentEnergy(){
        log("...setUserInterfaceCurrentEnergy()");
        int energy = MentalWorker.getEnergy(LocalDate.now(), this);
        String textEnergy = String.format(Locale.getDefault(), "energy: %d", energy);
        if( energy <= -3){
            textViewEnergy.setTextColor(Color.parseColor("#ff0000"));
        }else if( energy > - 3 && energy <= 2){
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