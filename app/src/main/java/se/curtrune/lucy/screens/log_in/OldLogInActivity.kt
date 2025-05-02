package se.curtrune.lucy.screens.log_in

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import se.curtrune.lucy.R
import se.curtrune.lucy.app.InitialScreen
import se.curtrune.lucy.app.Lucinda
import se.curtrune.lucy.app.UserPrefs
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.screens.index.IndexActivity
import se.curtrune.lucy.screens.main.MainActivity
import se.curtrune.lucy.util.Logger.Companion.log
import se.curtrune.lucy.workers.NotificationsWorker
import se.curtrune.lucy.workers.SettingsWorker
import java.sql.SQLException

/**
 * this is the first activity
 * initializes stuff
 * and starts user defined activity, today, week or month
 */
class OldLogInActivity : AppCompatActivity() {
    private var editTextPwd: EditText? = null
    private var buttonLogIn: Button? = null
    private var lucinda: Lucinda? = null
    private val userPrefs = LucindaApplication.appModule.userSettings
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("LogInActivity.onCreate(Bundle of joy)")
        setContentView(R.layout.log_in_activity)
        //initCatchAllExceptionsHandler()
        title = "lucinda"
        lucinda = Lucinda.getInstance(this)
        if (!lucinda!!.isInitialized(this)) {
            log("...lucinda not initialized")
            try {
                lucinda!!.initialize(this)
            } catch (e: SQLException) {
                Toast.makeText(this, "serious, failure to initialize the app", Toast.LENGTH_LONG)
                    .show()
                return
            }
        } else {
            log("Lucinda is initialized!")
        }
        if (!Lucinda.nightlyAlarmIsSet(this)) {
            Lucinda.setNightlyAlarm(this)
        } else {
            log("...nightly alarm is already set")
        }
        initComponents()
        initListeners()
        initDevMode()
        initDayNightMode()
        NotificationsWorker.createNotificationChannel(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        }
        if (UserPrefs.usesPassword(this) && !UserPrefs.isDevMode(this)) {
            log("...using password");
        } else {
            startUserActivity()
        }
        //}
    }

    private fun checkInternetConnection() {
        log("...checkInternetConnection()")
        val isConnected = LucindaApplication.appModule.internetWorker.isConnected()
        log("...isConnected", isConnected)
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPermission() {
        if (VERBOSE) log("...checkNotificationPermission()")
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            log("...POST_NOTIFICATIONS.PERMISSION_GRANTED")
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            //showInContextUI(...);
            log("should setMentalType request POST_NOTIFICATIONS permission rationale")
        } else {
            log("...will ask for POST_NOTIFICATIONS permissions ")
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 42)
        }
    }

    private fun initDayNightMode() {
        log("...initDayNightMode()")
        if (UserPrefs.getDarkMode(this)) {
            SettingsWorker.setDarkMode()
        } else {
            SettingsWorker.setLightMode()
        }
    }

    private fun initDevMode() {
        if (VERBOSE) log("...initDevMode()")
        Lucinda.Dev = UserPrefs.isDevMode(this)
        log("...devMode", Lucinda.Dev)
    }

    private fun initCatchAllExceptionsHandler() {
        Thread.setDefaultUncaughtExceptionHandler { paramThread: Thread?, paramThrowable: Throwable ->
            object : Thread() {
                override fun run() {
                    Looper.prepare()
                    paramThrowable.printStackTrace()
                    log(
                        " exception message",
                        paramThrowable.message
                    )
                    Toast.makeText(
                        this@OldLogInActivity,
                        paramThrowable.message,
                        Toast.LENGTH_LONG
                    ).show()
                    Looper.loop()
                }
            }.start()
            try {
                Thread.sleep(10000) // Let the Toast display before app will get shutdown
            } catch (e: InterruptedException) {
                log(e.message)
            }
            System.exit(2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        log("...onRequestPermissionsResult(...) requestCode ", requestCode)
        if (requestCode == 42) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                log("FUCK YEAH NOTIFICATIONS PERMISSION GRANTED")
            } else {
                log("SHIT NOTIFICATIONS PERMISSION DENIED")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initComponents() {
        editTextPwd = findViewById(R.id.logInActivity_pwd)
        buttonLogIn = findViewById(R.id.logInActivity_buttonLogIn)
    }

    private fun initListeners() {
        buttonLogIn!!.setOnClickListener { view: View? -> logIn() }
    }

    private fun logIn() {
        log("...logIn()")
        if (!validateInput()) {
            return
        }
        val pwd = editTextPwd!!.text.toString()
        if (UserPrefs.validatePassword("user", pwd, this)) {
            startUserActivity()
        } else {
            Toast.makeText(this, "incorrect password", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * configurable by user
     * at the time of writing this, the user can choose between going straight to the calender
     * or going to an index page with links to various activities/fragments
     */
    private fun startUserActivity() {
        log("...startUserActivity()")
        val initialScreen = UserPrefs.getInitialScreen(application)
        when (initialScreen) {
            InitialScreen.TODO_FRAGMENT -> {}
            InitialScreen.CALENDER_DATE -> TODO()
            InitialScreen.CALENDER_WEEK -> TODO()
            InitialScreen.CALENDER_MONTH -> TODO()
            InitialScreen.CALENDER_APPOINTMENTS -> TODO()
        }
    }

    private fun validateInput(): Boolean {
        log("...validateInput()")
        if (editTextPwd!!.text.toString().length < 8) {
            Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    companion object {
        var VERBOSE: Boolean = false
    }
}