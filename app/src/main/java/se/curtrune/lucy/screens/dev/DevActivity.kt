package se.curtrune.lucy.screens.dev

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.dev.ui.theme.LucyTheme
import se.curtrune.lucy.app.FirstPage
import se.curtrune.lucy.app.Lucinda
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.classes.Notification
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.CountDownTimerService
import se.curtrune.lucy.composables.MentalMeter4
import se.curtrune.lucy.composables.NavigationDrawer
import se.curtrune.lucy.composables.StopWatchUsingService
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.add_item.DefaultItemSettings
import se.curtrune.lucy.composables.add_item.TargetTimePicker
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.dialogs.RepeatDialog
import se.curtrune.lucy.persist.DBAdmin
import se.curtrune.lucy.persist.SqliteLocalDB
import se.curtrune.lucy.screens.dev.composables.BackupDataBase
import se.curtrune.lucy.screens.dev.composables.CalendarWeekTest
import se.curtrune.lucy.screens.dev.composables.DurationTest
import se.curtrune.lucy.screens.dev.composables.ExecuteQuery
import se.curtrune.lucy.screens.dev.composables.InsertItemWithID
import se.curtrune.lucy.screens.dev.composables.RepeatTest
import se.curtrune.lucy.screens.dev.composables.RepositoryTest
import se.curtrune.lucy.screens.dev.composables.TestGoogleCalendars
import se.curtrune.lucy.screens.dev.composables.TestScrollableYearMonth
import se.curtrune.lucy.screens.dev.composables.TestSwipeAble
import se.curtrune.lucy.screens.log_in.LogInActivity
import se.curtrune.lucy.screens.main.MainActivity
import se.curtrune.lucy.screens.main.TopAppBarState
import se.curtrune.lucy.screens.my_day.MyDayState
import se.curtrune.lucy.screens.repeat.RepeatActivity
import se.curtrune.lucy.services.TimerService
import se.curtrune.lucy.util.Constants
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.workers.NotificationsWorker
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale
import java.util.function.Consumer

class DevActivity : AppCompatActivity() {
    private val alarmRinging = false
    private var lucinda: Lucinda? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dev_activity)
        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        title = "lucinda dev"
        println("DevActivity.onCreate(Bundle)")
        //printSystemInfo()
        lucinda = Lucinda.getInstance(this)
        initContent()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    private fun initContent(){
        println("...initContent()")
        val composeView = findViewById<ComposeView>(R.id.devActivity_composeView)
        composeView?.setContent {
            val devViewModel = viewModel<DevViewModel>()
            val state = devViewModel.state.collectAsState()
            val context = LocalContext.current
            val mental = devViewModel.mental
            var showBottomSheet by remember {
                mutableStateOf(false)
            }
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            LaunchedEffect(devViewModel) {
                devViewModel.eventFlow.collect{ event->
                    when(event){
                        is DevChannel.NavigateToDayCalendar -> {
                            navigateToDayCalendar()
                        }
                        is DevChannel.ShowNavigationDrawer -> {
                            println("...showNavigationDrawer")
                            drawerState.open()
                        }
                        is DevChannel.NavigateToWeekCalendar -> {
                            navigate(FirstPage.CALENDER_WEEK)
                        }

                        is DevChannel.Navigate -> {
                            navigate(event.fragment)
                        }
                    }
                }
            }
            LucyTheme {
                val scope = rememberCoroutineScope()
                val scrollState = rememberScrollState()
                val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

                ModalNavigationDrawer(drawerState = drawerState,
                    drawerContent = {
                        NavigationDrawer(onEvent = { event->
                            devViewModel.onEvent(event)
                        })
                    }) {
                    Scaffold(
                        floatingActionButton = {
                            AddItemFab {
                                println("...floatingActionButton")
                                showBottomSheet = true
                            }
                        },
                        topBar = {
                            FlexibleTopBar(
                                scrollBehavior = scrollBehavior,
                                content = {
                                    LucindaTopAppBar(
                                        state = TopAppBarState(title = "dev"),
                                        onEvent = { appBarEvent ->
                                        devViewModel.onEvent(appBarEvent)
                                    })
                                }, onEvent = { event ->
                                    devViewModel.onEvent(event)
                                }
                            )
                        }

                    ) { padding ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(padding)
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.background)
                                    .verticalScroll(scrollState),
                                verticalArrangement = Arrangement.SpaceEvenly,
                            ) {
                                //DurationTest()
                                var testSwipeAbleItem by remember {
                                    mutableStateOf(false)
                                }
                                var showRepeatTest by remember {
                                    mutableStateOf(false)
                                }
                                var showScrollableYearMonth by remember {
                                    mutableStateOf(false)
                                }
                                var showRunSQL by remember {
                                    mutableStateOf(false)
                                }
                                var showItemDuration by remember {
                                    mutableStateOf(false)
                                }
                                var showStopwatchService by remember{
                                    mutableStateOf(false)
                                }
                                var showRepositoryTest by remember {
                                    mutableStateOf(false)
                                }
                                val showGoogleCalendar by remember {
                                    mutableStateOf(true)
                                }
                                val showBackupDataBase by remember {
                                    mutableStateOf(false)
                                }
                                val showCountDownService by remember {
                                    mutableStateOf(false)
                                }
                                //TargetTimePicker()
                                val item = Item().also {
                                    it.energy = 3
                                    it.anxiety =  2
                                    it.mood = 1
                                    it.stress = 0
                                    it.heading = "hello"
                                }
                                MentalMeter4(item = item, state = MyDayState() ) { event->
                                    println("...onEvent(event): ${event.toString()}")
                                }
                                if( showRunSQL){
                                    ExecuteQuery(onEvent = {event->
                                        devViewModel.onEvent(event)
                                    })
                                }
                                if( showRepositoryTest) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    RepositoryTest()
                                }
                                if( showGoogleCalendar) {
                                    TestGoogleCalendars()
                                }
                                if( showBackupDataBase) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    BackupDataBase(onEventCopy = {
                                        println("please copy database")
                                    })
                                }
                                //GetQuoteKtor()
                                //AffirmationTest()
                                //CheckForUpdateKtor()
                                Spacer(modifier = Modifier.height(16.dp))
                                CalendarWeekTest()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    modifier = Modifier.clickable {
                                        showItemDuration =!showItemDuration
                                    },
                                    text = "show item duration test",
                                    fontSize = 24.sp)
                                if(showItemDuration) {
                                    DurationTest()
                                }
                                Text(
                                    text = "test scrollable year month",
                                    fontSize = 24.sp,
                                    modifier = Modifier.clickable {
                                        showScrollableYearMonth = !showScrollableYearMonth
                                    })
                                AnimatedVisibility(visible = showScrollableYearMonth) {
                                    TestScrollableYearMonth()
                                }
                                Text(
                                    text = "test repeat",
                                    fontSize = 24.sp,
                                    modifier = Modifier.clickable {
                                        showRepeatTest = !showRepeatTest
                                    })
                                AnimatedVisibility(visible = showRepeatTest) {
                                    RepeatTest()
                                }
                                Text(
                                    text = "test swipeable item",
                                    fontSize = 24.sp,
                                    modifier = Modifier.clickable {
                                        testSwipeAbleItem = !testSwipeAbleItem
                                    }
                                )
                                AnimatedVisibility(visible = testSwipeAbleItem) {
                                    TestSwipeAble()
                                }

                                var showInsertItemWithId by remember {
                                    mutableStateOf(false)
                                }
                                Text(text = "insert item with id", fontSize = 24.sp,
                                    modifier = Modifier.clickable {
                                        showInsertItemWithId = !showInsertItemWithId
                                    })
                                AnimatedVisibility(
                                    visible = showInsertItemWithId
                                ) {
                                    InsertItemWithID(onEvent = { event ->
                                        devViewModel.onEvent(event)
                                    })
                                }
                                //Column()
                                /*                            ItemsTabsTest(state = state.value, onEvent = { event->
                                devViewModel.onEvent(event)
                            })*/
                                Text(text = "stopwatch service", fontSize = 24.sp,
                                    modifier = Modifier.clickable {
                                        showStopwatchService = !showStopwatchService
                                    })
                                Spacer(modifier = Modifier.height(16.dp))
                                AnimatedVisibility(visible = showStopwatchService) {
                                    StopWatchUsingService(onCommand = { action ->
                                        println("command: $action")
                                        sendCommandToTimeService(action)
                                    })
                                }

                                if( showCountDownService) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    CountDownTimerService(15, onCommand = { command, duration ->
                                        sendCommandToTimeService(command, duration)
                                    })
                                }
                            }
                            if( showBottomSheet){
                                AddItemBottomSheet(
                                    defaultItemSettings = DefaultItemSettings(),
                                    onDismiss = {
                                    println("...onDismiss")
                                    showBottomSheet = false
                                }, onSave ={ item->
                                    println("...onSave: ${item.heading}")
                                    devViewModel.onEvent(DevEvent.InsertItem(item))
                                    showBottomSheet = false
                                } )
                            }
                        }
                    }
                }
            }
        }
    }
    private fun sendCommandToTimeService(command: String){
        val timeModule = LucindaApplication.appModule.timeModule
        println("DevActivity.sendCommandToService $command")
        timeModule.sendCommand(command)
/*        Intent( this, TimerService::class.java).also {
            it.action = command
            this.startService(it)
        }*/
    }
    private fun sendCommandToTimeService(command: String, duration: Long){
        println("DevActivity.sendCommandToService $command")
        Intent( this, TimerService::class.java).also {
            it.action = command
            if(command == TimerService.ACTION_START_COUNTDOWN_TIMER){
                it.putExtra("COUNTDOWN_DURATION", duration)
            }
            this.startService(it)
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
        when (item.itemId) {
            R.id.homeActivity_openDB -> {
                openDB()
            }
            R.id.homeActivity_createTableItems -> {
                DBAdmin.createItemsTable(this)
            }
            R.id.homeActivity_dropTableItems -> {
                DBAdmin.dropTableItems(this)
            }
            R.id.homeActivity_listTables -> {
                listTables()
            }
            R.id.homeActivity_resetApp -> {
                resetApp()
            }
            R.id.homeActivity_createEconomyTables -> {
                DBAdmin.createEconomyTables(this)
            }
            R.id.devActivity_testNotification -> {
                testNotification()
            }
            R.id.homeActivity_logInActivity -> {
                startActivity(Intent(this, LogInActivity::class.java))
            }
            R.id.devActivity_userSettings -> {
                setDefaultUserSettings()
            }
            R.id.devActivity_clearSettings -> {
                //Settings.removeAll(this);
                Toast.makeText(this, "don't do this", Toast.LENGTH_LONG).show()
            }
            R.id.devActivity_repeatDialog -> {
                showRepeatDialog()
                Toast.makeText(this, "for future use", Toast.LENGTH_LONG).show()
            }
            R.id.homeActivity_setNotifications -> {
                setNotifications()
            }
            R.id.devActivity_repeatActivity -> {
                startActivity(Intent(this, RepeatActivity::class.java))
            }
            R.id.devActivity_home -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return true
    }

    private fun openDB() {
        Logger.log("...openDB")
        val db = SqliteLocalDB(this)
        db.open()
    }

    private fun resetApp() {
        println("...resetApp()")

/*        try {
            lucinda!!.reset(this)
            Toast.makeText(this, "lucinda reset", Toast.LENGTH_LONG).show()
        } catch (e: SQLException) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }*/
    }


    private fun runCode() {
        Logger.log("...runCode()")
/*        val repository = LucindaApplication.repository
        val calendarMonth = repository.getCalendarMonth(YearMonth.now(), applicationContext)
        val item = Item()
        item.heading = "test calendarMonth"
        item.targetDate = LocalDate.now()
        item.targetTime = LocalTime.now()
        item.setIsCalenderItem(true)
        calendarMonth.addEvent(item)
        val calendarDate = calendarMonth.getCalenderDate(LocalDate.now())
        calendarDate.items.forEach{ dateItem->
            println(dateItem.toString())
        }*/

    //RepeatTest.createRepeatItemDecember(this);
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
    private fun navigateToDayCalendar() {
        startActivity(Intent(this, MainActivity::class.java))
    }
    private fun navigate(firstPage: FirstPage){
        println("DevActivity.navigate(firstPage: ${firstPage.name})")
        val intentTodo = Intent(
            this,
            MainActivity::class.java
        )
        intentTodo.putExtra(Constants.MAIN_ACTIVITY_CHILD_FRAGMENT, firstPage.toString())
        startActivity(intentTodo)
    }


    private fun printTableNames() {
        Logger.log("...printTableNames()")
        SqliteLocalDB(this).use { db ->
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
            Logger.log(repeat.toString())
        }
        dialog.show(supportFragmentManager, "repeat dialog")
    }

    private fun testNotification() {
        println("...testNotification()")
        val repository = LucindaApplication.appModule.repository
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
        val todoParent = repository.getRootItem(Settings.Root.TODO)
        if( todoParent == null){
            println("todoParent == null")
            return
        }
        item.parentId = todoParent.id
        item.notification = notification
        item = repository.insertChild(todoParent, item)
        Logger.log(item)
        NotificationsWorker.setNotification(item, this)
    }

    companion object {
        var VERBOSE: Boolean = false
    }
}
