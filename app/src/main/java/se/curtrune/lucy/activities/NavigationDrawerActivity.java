package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import se.curtrune.lucy.R;

public class NavigationDrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_actvity);
        log("NavigationDrawerActivity.onCreate(Bundle of joy)");
        initComponents();
    }
    private void initComponents(){
        log("...initComponents()");
        //toolbar = findViewById(R.id.navigationDrawer_toolbar);
        //setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigationDrawerActivity_navigationView);
        drawerLayout = findViewById(R.id.navigationDrawer_drawerLayout);
        navigationView = findViewById(R.id.navigationDrawerActivity_navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}