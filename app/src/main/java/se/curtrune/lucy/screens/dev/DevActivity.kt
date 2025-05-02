package se.curtrune.lucy.screens.dev

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.dev.ui.theme.LucyTheme
import se.curtrune.lucy.app.InitialScreen
import se.curtrune.lucy.app.Lucinda
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.classes.Notification
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.CountDownTimerService
import se.curtrune.lucy.composables.NavigationDrawer
import se.curtrune.lucy.composables.StopWatchUsingService
import se.curtrune.lucy.composables.add_item.AddItemBottomSheet
import se.curtrune.lucy.composables.add_item.DefaultItemSettings
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.dialogs.RepeatDialog
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.persist.DBAdmin
import se.curtrune.lucy.persist.SqliteLocalDB
import se.curtrune.lucy.screens.dev.composables.BackupDataBase
import se.curtrune.lucy.screens.dev.composables.CalendarWeekTest
import se.curtrune.lucy.screens.dev.composables.DevScreen
import se.curtrune.lucy.screens.dev.composables.DurationTest
import se.curtrune.lucy.screens.dev.composables.InsertItemWithID
import se.curtrune.lucy.screens.dev.composables.OpenDB
import se.curtrune.lucy.screens.dev.composables.RepeatTest
import se.curtrune.lucy.screens.dev.composables.TestScrollableYearMonth
import se.curtrune.lucy.screens.dev.composables.TestSwipeAble
import se.curtrune.lucy.screens.log_in.OldLogInActivity
import se.curtrune.lucy.screens.log_in.composables.PasswordTextField
import se.curtrune.lucy.screens.main.MainActivity
import se.curtrune.lucy.screens.main.TopAppBarState
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
                            navigate(InitialScreen.CALENDER_WEEK)
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
                                        }
                                    )
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
                            DevScreen(onEvent = { event ->
                                devViewModel.onEvent(event)
                            })
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


    private fun openDB() {
        println("...openDB")
        val db = SqliteLocalDB(this)
        db.open()
    }

    private fun resetApp() {
        println("...resetApp()")
    }



    private fun navigateToDayCalendar() {
        startActivity(Intent(this, MainActivity::class.java))
    }
    private fun navigate(firstPage: InitialScreen){
        println("DevActivity.navigate(firstPage: ${firstPage.name})")
        val intentTodo = Intent(
            this,
            MainActivity::class.java
        )
        intentTodo.putExtra(Constants.INITIAL_SCREEN, firstPage.toString())
        startActivity(intentTodo)
    }


    private fun sendCommand(command: String){
        println("...sendCommand($command)")
        Intent( this, TimerService::class.java).also {
            it.action = command
            startService(it)
        }
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
