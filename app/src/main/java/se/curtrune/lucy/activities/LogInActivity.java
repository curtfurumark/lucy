package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.sql.SQLException;

import se.curtrune.lucy.R;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.app.User;
import se.curtrune.lucy.workers.NotificationsWorker;

public class LogInActivity extends AppCompatActivity {
    private EditText editTextUser;
    private EditText editTextPwd;
    private Button buttonLogIn;
    private CheckBox checkBoxUsePassword;
    private Lucinda lucinda;
    private boolean createPassword = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("LogInActivity.onCreate(Bundle of joy");
        setContentView(R.layout.log_in_activity);
        initCatchAllExceptionsHandler();
        setTitle("lucinda");
        log("LogInActivity.onCreate(Bundle)");
        printSystemInfo();
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
        initComponents();
        initListeners();
        checkNotificationPermission();
        NotificationsWorker.createNotificationChannel(this);
        startActivity(new Intent( this, MainActivity.class));
        //clearPassword();


/*checkStuff();
  if( User.usesPassword(this)){
            log("...using password");
            if( User.hasPassword(this)){
                validateUser();
            }else{
                createPassword();
            }
        }else{
            log("...not using password");
            startActivity(new Intent(this, MainActivity.class));
        }*/
    }
    private void checkNotificationPermission() {
        log("...checkNotificationPermission()");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            log("PERMISSION_GRANTED");
        } else {
            log("PERMISSION_DENIED");
        }
    }
    private void checkStuff(){
        log("...checkStuff()");
        boolean usesPassword = User.usesPassword(this);
        log("...usesPassword", usesPassword);
        String pwd = User.getPassword(this);
        log("...pwd", pwd);
    }
    private void clearPassword(){
        User.setUsesPassword(true, this);
        //User.savePassword("", this);
    }
    private void initCatchAllExceptionsHandler(){
        Thread.setDefaultUncaughtExceptionHandler((paramThread, paramThrowable) -> {

            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(LogInActivity.this,paramThrowable.getMessage(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
            try
            {
                Thread.sleep(4000); // Let the Toast display before app will get shutdown
            }
            catch (InterruptedException e) {
                log(e.getMessage());
            }
            System.exit(2);
        });
    }
    private void printSystemInfo(){
        log("...printSystemInfo()");
        log("\tSDK_INT", Build.VERSION.SDK_INT);
        log("\tDEVICE", Build.DEVICE);
        log("\tUSER", Build.USER);
        log("\tHARDWARE", Build.HARDWARE);
        log("\tBRAND", Build.BRAND);
    }
    private void validateUser(){
        log("...validateUser()");
        String pwd = editTextPwd.getText().toString();
        String user = editTextUser.getText().toString();
        if( User.validatePassword(user, pwd, this)){
            startActivity(new Intent(this, MainActivity.class));
        }else{
            log("...ERROR validating password");
            Toast.makeText(this, "ERROR password", Toast.LENGTH_LONG).show();
        }
    }
    private void createPassword(){
        log("...createPassword");
        buttonLogIn.setText(R.string.create_password);
        Toast.makeText(this, "PASSWORD has to be at least 8 characters", Toast.LENGTH_LONG).show();
        createPassword = true;

    }
    private void initComponents(){
        editTextPwd = findViewById(R.id.logInActivity_pwd);
        editTextUser = findViewById(R.id.logInActivity_user);
        buttonLogIn = findViewById(R.id.logInActivity_buttonLogIn);
        checkBoxUsePassword = findViewById(R.id.logInActivity_checkBoxUsePassword);
    }
    private void initListeners(){
        buttonLogIn.setOnClickListener(view->logIn());
    }
    private void logIn(){
        log("...logIn()");
        if( !validateInput()){
            return;
        }
        if( createPassword){
            User.setUsesPassword(true, this);
            User.savePassword(editTextPwd.getText().toString(), this);
            startActivity(new Intent(this, MainActivity.class));
        }else if(User.validatePassword(editTextUser.getText().toString(), editTextPwd.getText().toString(), this)){
            startActivity(new Intent(this, MainActivity.class));
        }else{
            Toast.makeText(this, "wrong password", Toast.LENGTH_LONG).show();
        }
    }
    private boolean validateInput(){
        if( editTextUser.getText().toString().isEmpty()){
            Toast.makeText(this, "no user name supplied", Toast.LENGTH_LONG).show();
            return false;
        }
        if( editTextPwd.getText().toString().length() < 8){
            Toast.makeText(this, "password must be at least 8 characters long", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}