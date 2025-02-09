package se.curtrune.lucy.screens.dev

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.query
import kotlinx.coroutines.launch
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.R
import se.curtrune.lucy.screens.RepeatActivity
import se.curtrune.lucy.activities.kotlin.dev.ui.theme.LucyTheme
import se.curtrune.lucy.screens.week_calendar.WeekCalendarActivityKt
import se.curtrune.lucy.app.Lucinda
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.MediaContent
import se.curtrune.lucy.classes.Notification
import se.curtrune.lucy.composables.CategoryStatistics
import se.curtrune.lucy.composables.CountDownTimerService
import se.curtrune.lucy.composables.Search
import se.curtrune.lucy.composables.StopWatchUsingService
import se.curtrune.lucy.dialogs.RepeatDialog
import se.curtrune.lucy.persist.DBAdmin
import se.curtrune.lucy.persist.ItemsWorker
import se.curtrune.lucy.persist.LocalDB
import se.curtrune.lucy.screens.affirmations.RetrofitInstance
import se.curtrune.lucy.screens.common.MentalMeter
import se.curtrune.lucy.screens.dev.composables.BackupDataBase
import se.curtrune.lucy.screens.dev.composables.CreateItemTree
import se.curtrune.lucy.screens.dev.composables.DurationByCategory
import se.curtrune.lucy.screens.dev.composables.GetNumberOfChildren
import se.curtrune.lucy.screens.dev.composables.GetQuote
import se.curtrune.lucy.screens.dev.composables.MentalMeterTest
import se.curtrune.lucy.screens.dev.composables.RepositoryTest
import se.curtrune.lucy.screens.dev.composables.SetGeneratedToTemplateChildren
import se.curtrune.lucy.screens.item_editor.composables.ItemEditorDev
import se.curtrune.lucy.screens.log_in.LogInActivity
import se.curtrune.lucy.screens.main.MainActivity
import se.curtrune.lucy.screens.main.MainState
import se.curtrune.lucy.screens.main.composables.LucindaControls
import se.curtrune.lucy.screens.util.Converter
import se.curtrune.lucy.services.TimerService
import se.curtrune.lucy.statistics.Statistics
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.viewmodel.UpdateLucindaViewModel
import se.curtrune.lucy.web.VersionInfo
import se.curtrune.lucy.workers.NotificationsWorker
import java.sql.SQLException
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.util.Locale
import java.util.Timer
import java.util.function.Consumer

class DevActivity : AppCompatActivity() {
    private val alarmRinging = false
    private var lucinda: Lucinda? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dev_activity)
        title = "lucinda dev"
        println("DevActivity.onCreate(Bundle)")
        printSystemInfo()
        lucinda = Lucinda.getInstance(this)
        initContent()
    }

    private fun clearShowInCalendar() {
        Logger.log("...clearShowInCalendar()")
        val queery = "UPDATE items set isCalenderItem = 0"
        try {
            LocalDB(this).use { db ->
                db.executeSQL(queery)
                Toast.makeText(this, "items updated", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkForLucindaUpdate() {
        Logger.log("...checkForLucindaUpdate()")
        val updateLucindaViewModel = ViewModelProvider(this)[UpdateLucindaViewModel::class.java]
        updateLucindaViewModel.checkForNewVersion()
        updateLucindaViewModel.versionInfo.observe(this, object : Observer<VersionInfo?> {
            override fun onChanged(value: VersionInfo?) {
                Logger.log("...onChanged, VersionInfo available", value.toString())
            }
        })
    }
    private fun copyDatabase(){
        println("DevActivity.copyDatabase()")
        val dbFile = getDatabasePath(LocalDB.getDbName())
        println("...absolute path ${dbFile.absolutePath}")

    }

    private fun initContent(){
        println("...initContent()")
        val composeView = findViewById<ComposeView>(R.id.devActivity_composeView)
        composeView?.setContent {
            val devViewModel = viewModel<DevActivityViewModel>()
            val state = devViewModel.state.collectAsState()
            val context = LocalContext.current
            val mental = devViewModel.mental
            LucyTheme {
                val scope = rememberCoroutineScope()
                val scrollState = rememberScrollState()
                Surface(
                    modifier = Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    //SystemInfoList(state = state.value)
                    Column(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        Search(onSearch = { filter, everywhere->
                            println("search query: $filter")
                            devViewModel.onEvent(DevEvent.Search(filter, everywhere))
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                        val stats = Statistics(LucindaApplication.repository.selectItems(LocalDate.now()))
                        CategoryStatistics(stats)
                        Spacer(modifier = Modifier.height(16.dp))
                        //GetQuote()
                        //RepositoryTest()
                        //Spacer(modifier = Modifier.height(16.dp))
                        StopWatchUsingService(onCommand = { action->
                            println("command: $action")
                            sendCommandToTimeService(action)
                        })
                        val mentalModule = LucindaApplication.mentalModule
                        //MentalMeterTest(state = state.value)
                        //Spacer(modifier = Modifier.height(8.dp))
                        LucindaControls(state = MainState(), onEvent = { event ->
                            Toast.makeText(applicationContext,event.toString(), Toast.LENGTH_LONG ).show()
                        })

                        BackupDataBase(onEventCopy = {
                            println("copy database ")
                            copyDatabase()
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                        SetGeneratedToTemplateChildren(state = state.value, onEvent = { event ->
                            devViewModel.onEvent(event)
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                        ItemEditorDev(item = Item("i am item"), onEvent = { event->
                            devViewModel.onEvent(event)
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                        //DurationByCategory()
                        //Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "main", fontSize = 24.sp, modifier = Modifier.clickable {
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                        //Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "run test", fontSize = 24.sp,
                            modifier = Modifier.clickable {
                                runCode()
                            })
                        Spacer(modifier = Modifier.height(16.dp))

                        Spacer(modifier = Modifier.height(16.dp))
                        //MentalMeter()
                        //Spacer(modifier = Modifier.height(16.dp))
                        CountDownTimerService(duration = 30, onCommand = {
                            println("command: $it")
                            sendCommandToTimeService(it)
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                        GetNumberOfChildren()
                        Spacer(modifier = Modifier.height(16.dp))
                        CreateItemTree(onEvent = { event->
                            devViewModel.onEvent(event)
                        })
                        //Spacer(modifier = Modifier.height(16.dp))
                        //SystemInfoList(state = state.value)
                    }
                }
            }
        }
    }
    private fun sendCommandToTimeService(command: String){
        println("DevActivity.sendCommandToService $command")
        Intent( this, TimerService::class.java).also {
            it.action = command
            this.startService(it)
        }
    }

    private fun printSystemInfo() {
        Logger.log("...printSystemInfo()")
        Logger.log("\tSDK_INT", Build.VERSION.SDK_INT)
        Logger.log("\tDEVICE", Build.DEVICE)
        Logger.log("\tUSER", Build.USER)
        Logger.log("\tHARDWARE", Build.HARDWARE)
        Logger.log("\tBRAND", Build.BRAND)
        Logger.log("\tMANUFACTURER", Build.MANUFACTURER)
        Logger.log("\tMODEL", Build.MODEL)
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            //String version = pInfo.versionName;
            Logger.log("...versionName", pInfo.versionName)
            Logger.log("...versionCode", pInfo.versionCode)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Logger.log("...long versionCode", pInfo.longVersionCode)
            }
            Logger.log("...packageName", pInfo.packageName)
            val firstInstallTime = pInfo.firstInstallTime
            Logger.log("firstInstallTime", firstInstallTime)
            Logger.log("converted install time", Converter.epochToDate(firstInstallTime / 1000))
            val applicationInfo = pInfo.applicationInfo
            if (applicationInfo != null) {
                Logger.log("...dataDir", applicationInfo.dataDir)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun listTables() {
        Logger.log("...listTables()")
        DBAdmin.getTableNames(this).forEach(Consumer { x: String? -> println(x) })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dev_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.homeActivity_openDB) {
            openDB()
        } else if (item.itemId == R.id.homeActivity_createTableItems) {
            DBAdmin.createItemsTable(this)
        } else if (item.itemId == R.id.homeActivity_dropTableItems) {
            DBAdmin.dropTableItems(this)
        } else if (item.itemId == R.id.homeActivity_listTables) {
            listTables()
        } else if (item.itemId == R.id.homeActivity_dropTableMental) {
            DBAdmin.dropTableMental(this)
        } else if (item.itemId == R.id.homeActivity_resetApp) {
            resetApp()
        } else if (item.itemId == R.id.homeActivity_createEconomyTables) {
            DBAdmin.createEconomyTables(this)
        } else if (item.itemId == R.id.devActivity_testNotification) {
            testNotification()
        } else if (item.itemId == R.id.homeActivity_logInActivity) {
            startActivity(Intent(this, LogInActivity::class.java))
        } else if (item.itemId == R.id.devActivity_userSettings) {
            setDefaultUserSettings()
        } else if (item.itemId == R.id.devActivity_clearSettings) {
            //Settings.removeAll(this);
            Toast.makeText(this, "don't do this", Toast.LENGTH_LONG).show()
        } else if (item.itemId == R.id.devActivity_repeatDialog) {
            showRepeatDialog()
            Toast.makeText(this, "for future use", Toast.LENGTH_LONG).show()
        } else if (item.itemId == R.id.homeActivity_setNotifications) {
            setNotifications()
        } else if (item.itemId == R.id.devActivity_repeatActivity) {
            startActivity(Intent(this, RepeatActivity::class.java))
        } else if (item.itemId == R.id.devActivity_weekCalendarActivity) {
            startActivity(Intent(this, WeekCalendarActivityKt::class.java))
        }
        return true
    }

    private fun openDB() {
        Logger.log("...openDB")
        val db = LocalDB(this)
        db.open()
    }

    private fun resetApp() {
        Logger.log("...resetApp()")
        try {
            lucinda!!.reset(this)
            Toast.makeText(this, "lucinda reset", Toast.LENGTH_LONG).show()
        } catch (e: SQLException) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }


    private fun creteItemWithMedia() {
        Logger.log("...createItemWithMedia()")
        var item = Item("item with media")
        val media = MediaContent()
        media.setFileType(MediaContent.FileType.TEXT)
        media.setFilePath("dkjdkj")
        item.content = media
        item = ItemsWorker.insertChild(
            ItemsWorker.getRootItem(
                Settings.Root.PROJECTS,
                this
            ), item, this
        )
        Logger.log("...item inserted with id", item.id)
    }

    private fun runCode() {
        Logger.log("...runCode()")
        val calendarMonth = CalendarMonthTest.getCalendarMonth(YearMonth.now(), applicationContext)
        val item = Item()
        item.heading = "test calendarMonth"
        item.targetDate = LocalDate.now()
        item.targetTime = LocalTime.now()
        item.setIsCalenderItem(true)
        calendarMonth.addEvent(item)
        val calendarDate = calendarMonth.getCalenderDate(LocalDate.now())
        calendarDate.items.forEach{ dateItem->
            println(dateItem.toString())
        }        //RepeatTest.createRepeatItemDecember(this);
        //RepeatTest.selectRepeats(this);
        //RepeatTest.listInstances(6, this);
        //RepeatTest.setLastDate(6, LocalDate.of(2024, 12, 22), this);
        //RepeatTest.updateRepeats(this);
        //RepeatTest.listRepeats(this)

        //Item item = ItemsWorker.selectItem(4244, this);
        //log("...item", item.getHeading());
        //deleteTree(item)
        /*        if( alarmRinging){
            AlarmTest.stopAlarm();
            alarmRinging = false;
            buttonRunCode.setText("start alarm");
        }else {
            alarmRinging = true;
            buttonRunCode.setText("stop alarm");
            AlarmTest.soundAlarm(this);
        }*/
        //RepeatTest.selectRepeats(this);
        //ItemsTest.testDeleteTree(this);
        //ItemsTest.createTree(this);
        //ItemsTest.deleteTree(4924, this);
        /*        if( NotificationTest.notificationSet){
            log("will cancel alarm");
            NotificationTest.cancelAlarm(NotificationTest.notificationItem.getID(), this);
            Toast.makeText(this, "notification cancelled????", Toast.LENGTH_LONG).show();
        }else {
            LocalDateTime dateTime = LocalDateTime.now().plusMinutes(5);
            String heading = String.format(Locale.getDefault(), "notification %s", dateTime.toString());
            NotificationTest.setNotification(heading, "hello content", dateTime, this);
            buttonRunCode.setText("cancel alarm");
            Toast.makeText(this, "notification set", Toast.LENGTH_LONG).show();
        }*/
        /*        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.SET_ALARM) == PackageManager.PERMISSION_GRANTED){
            log("...got myself permission to set alarm, yeah");
            AlarmTest.setAlarmUsingIntent(this, 16, 40);
        }else{
            Toast.makeText(this, "need to ask permission to set alarm", Toast.LENGTH_LONG).show();
        }*/

        //showAddItemDialog();
    }

    private fun printTableNames() {
        Logger.log("...printTableNames()")
        LocalDB(this).use { db ->
            val names = db.tableNames
            names.forEach(Consumer { x: String? -> println(x) })
        }
    }
    private fun sendCommand(command: String){
        println("...sendCommand($command)")
        Intent( this, TimerService::class.java).also {
            it.action = command
            startService(it)
        }
    }

    private fun setDefaultUserSettings() {
        Logger.log("...setDefaultUserSettings")
        Lucinda.setDefaultUserSettings(this)
    }

    private fun setNotifications() {
        Logger.log("...setNotifications()")
        NotificationsWorker.setNotifications(LocalDate.now(), this)
    }

    private fun showRepeatDialog() {
        Logger.log("...showRepeatDialog()")
        val dialog = RepeatDialog()
        dialog.setCallback { repeat ->
            Logger.log("...onRepeat(Repeat)", repeat.toString())
            Logger.log(repeat)
        }
        dialog.show(supportFragmentManager, "repeat dialog")
    }

    private fun testNotification() {
        Logger.log("...testNotification()")
        val notification = Notification()
        val targetTime = LocalTime.now().plusMinutes(5)
        notification.time = targetTime
        notification.date = LocalDate.now()
        var item = Item()
        val heading =
            String.format(Locale.getDefault(), "notify me %s", notification.time.toString())
        item.heading = heading
        item.targetDate = LocalDate.now()
        item.targetTime = targetTime
        val todoParent = ItemsWorker.getRootItem(Settings.Root.TODO, this)
        item.parentId = todoParent.id
        item.notification = notification
        item = ItemsWorker.insertChild(todoParent, item, this)
        Logger.log(item)
        NotificationsWorker.setNotification(item, this)
    }

    companion object {
        var VERBOSE: Boolean = false
    }
}
