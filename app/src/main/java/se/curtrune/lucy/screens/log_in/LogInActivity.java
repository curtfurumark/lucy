package se.curtrune.lucy.screens.log_in;

import static se.curtrune.lucy.util.Logger.log;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.sql.SQLException;

import se.curtrune.lucy.LucindaApplication;
import se.curtrune.lucy.R;
import se.curtrune.lucy.app.UserPrefs;
import se.curtrune.lucy.screens.main.MainActivity;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.screens.index.IndexActivity;
import se.curtrune.lucy.workers.InternetWorker;
import se.curtrune.lucy.workers.NotificationsWorker;
import se.curtrune.lucy.workers.SettingsWorker;

/**
 * this is the first activity
 * initializes stuff
 * and starts user defined activity, today, week or month
 */

public class LogInActivity extends AppCompatActivity {
    private EditText editTextPwd;
    private Button buttonLogIn;
    private Lucinda lucinda;
    public static boolean VERBOSE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("LogInActivity.onCreate(Bundle of joy)");
        setContentView(R.layout.log_in_activity);
        initCatchAllExceptionsHandler();
        setTitle("lucinda");
        lucinda = Lucinda.getInstance(this);
        if (!lucinda.isInitialized(this)) {
            log("...lucinda not initialized");
            try {
                lucinda.initialize(this);
            } catch (SQLException e) {
                Toast.makeText(this, "serious, failure to initialize the app", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            log("Lucinda is initialized!");
        }
        if( !Lucinda.nightlyAlarmIsSet(this)){
            Lucinda.setNightlyAlarm(this);
        }else{
            log("...nightly alarm is already set");
        }
        initComponents();
        initListeners();
        initDevMode();
        initDayNightMode();
        checkInternetConnection();
        //updateRepeats();
        NotificationsWorker.createNotificationChannel(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission();
        }
       /* if (UserPrefs.usesPassword(this) && !UserPrefs.isDevMode(this)) {
            log("...using password");
        } else {
*/            startUserActivity();
        //}
    }

    private void checkInternetConnection(){
        log("...checkInternetConnection()");
        boolean isConnected = LucindaApplication.internetWorker.isConnected();
        log("...isConnected", isConnected);
    }
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkNotificationPermission() {
        if( VERBOSE) log("...checkNotificationPermission()");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            log("...POST_NOTIFICATIONS.PERMISSION_GRANTED");
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.POST_NOTIFICATIONS)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            //showInContextUI(...);
            log("should setMentalType request POST_NOTIFICATIONS permission rationale");
        } else {
            log("...will ask for POST_NOTIFICATIONS permissions ");
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 42);
        }
    }

    private void initDayNightMode(){
        log("...initDayNightMode()");
        if (UserPrefs.getDarkMode(this)){
            SettingsWorker.setDarkMode();
        }else {
            SettingsWorker.setLightMode();
        }

    }

    private void initDevMode(){
        if( VERBOSE) log("...initDevMode()");
        Lucinda.Dev = UserPrefs.isDevMode(this);
        log("...devMode", Lucinda.Dev);

    }

    private void initCatchAllExceptionsHandler(){
        Thread.setDefaultUncaughtExceptionHandler((paramThread, paramThrowable) -> {

            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    paramThrowable.printStackTrace();
                    log(" exception message", paramThrowable.getMessage());
                    Toast.makeText(LogInActivity.this,paramThrowable.getMessage(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
            try
            {
                Thread.sleep(10000); // Let the Toast display before app will get shutdown
            }
            catch (InterruptedException e) {
                log(e.getMessage());
            }
            System.exit(2);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        log("...onRequestPermissionsResult(...) requestCode ", requestCode);
        if( requestCode == 42){
            if( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                log("FUCK YEAH NOTIFICATIONS PERMISSION GRANTED");
            }else{
                log("SHIT NOTIFICATIONS PERMISSION DENIED");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initComponents(){
        editTextPwd = findViewById(R.id.logInActivity_pwd);
        buttonLogIn = findViewById(R.id.logInActivity_buttonLogIn);
    }
    private void initListeners(){
        buttonLogIn.setOnClickListener(view->logIn());
    }
    private void logIn(){
        log("...logIn()");
        if( !validateInput()){
            return;
        }
        String pwd = editTextPwd.getText().toString();
        if( UserPrefs.validatePassword("user", pwd, this)){
            startUserActivity();
        }else{
            Toast.makeText(this, "incorrect password", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * configurable by user
     * at the time of writing this, the user can choose between going straight to the calender
     * or going to an index page with links to various activities/fragments
     */
    private void startUserActivity(){
        log("...startUserActivity()");
        Settings.StartActivity startActivity = UserPrefs.getStartActivity(this);
        switch (startActivity){
            case INDEX_ACTIVITY:
                startActivity(new Intent(this, IndexActivity.class));
                break;
            case TODAY_ACTIVITY:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
    private boolean validateInput(){
        log("...validateInput()");
        if( editTextPwd.getText().toString().length() < 8){
            Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}